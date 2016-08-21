package com.kulikowski.todos.database

import com.kulikowski.todos.model.Task
import io.realm.*
import rx.Observable

class ToDosRepository(private val realmProvider: RealmProvider) {

    fun getAllTasksObservable(): Observable<RealmResults<Task>> = getTasksObservable(false)

    fun getModifiedTasksObservable(): Observable<RealmResults<Task>> = getTasksObservable(true)

    fun getTask(taskId: Int): Observable<Task> {
        val realm = realmProvider.getRealmInstance()
        return findFirstTaskAsync(realm, taskId)
                .asObservable<Task>()
                .closeRealmInstance(realm)
                .filter { it -> it.isLoaded }
                .map { task -> realm.copyFromRealm(task) }
    }

    fun getModifiedTasks(): Observable<List<Task>> {
        val realm = realmProvider.getRealmInstance()
        val objects = realm.copyFromRealm(findAllModifiedTasks(realm))
        //http://blog.danlew.net/2015/07/23/deferring-observable-code-until-subscription-in-rxjava/
        return Observable.defer { Observable.just(objects) }
    }

    fun updateTask(task: Task) {
        val realm = realmProvider.getRealmInstance()
        copyToRealm(realm, task)
    }

    fun copyOrUpdateTasks(tasks: List<Task>) {
        val realm = realmProvider.getRealmInstance()
        copyToRealm(realm, tasks)
    }

    private fun getTasksObservable(modifiedOnly: Boolean): Observable<RealmResults<Task>> {
        val realm = realmProvider.getRealmInstance()
        return findAllAsyncTasks(realm, modifiedOnly)
                .asObservable()
                .closeRealmInstance(realm)
                .filter { it -> it.isLoaded }
    }

    private fun findAllAsyncTasks(realm: Realm, modifiedOnly: Boolean): RealmResults<Task> =
            getTaskBasedQuery(realm, modifiedOnly).findAllAsync()


    private fun findFirstTaskAsync(realm: Realm, taskId: Int) =
            getTaskBasedQuery(realm, false).equalTo(Task.ID_KEY, taskId).findFirstAsync()

    private fun findAllModifiedTasks(realm: Realm) = getTaskBasedQuery(realm, true).findAll()

    private fun getTaskBasedQuery(realm: Realm, onlyModified: Boolean): RealmQuery<Task> {
        val query = realm.where(Task::class.java)
        if (onlyModified) query.equalTo(Task.MODIFIED_KEY, true)
        return query
    }

    private fun copyToRealm(realm: Realm, realmObject: RealmModel) {
        realm.beginTransaction()
        realm.copyToRealmOrUpdate(realmObject)
        realm.commitTransaction()
        realm.close()
    }

    private fun copyToRealm(realm: Realm, realmObjects: Iterable<RealmModel>) {
        realm.beginTransaction()
        realm.copyToRealmOrUpdate(realmObjects)
        realm.commitTransaction()
        realm.close()
    }
}


