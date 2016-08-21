package com.kulikowski.todos.database

import io.realm.Realm
import rx.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealmProvider @Inject constructor() {
    fun getRealmInstance(): Realm = Realm.getDefaultInstance()
}

fun <T> Observable<T>.closeRealmInstance(realm: Realm): Observable<T> =
        doOnUnsubscribe { realm.close() }.doOnError { realm.close() }
