package com.kulikowski.todos.domain

import android.os.Bundle
import com.kulikowski.todos.database.ToDosRepository
import com.kulikowski.todos.model.Task
import com.kulikowski.todos.network.ToDoApiService
import io.realm.RealmResults
import retrofit2.Response
import rx.Observable
import java.util.*


class TasksUseCase(private val toDoApiService: ToDoApiService, private val toDosRepository: ToDosRepository) {
    private val totalCountHeader = "X-Total-Count"
    private val totalCountKey = "total-count-key"
    private val lastEndKey = "last-start-key"
    private var totalCount: Int = 0
    private var lastEnd: Int = 0

    fun hasNext(): Boolean = totalCount > lastEnd

    fun onSaveInstanceState(outState: Bundle?) {
        outState?.putInt(totalCountKey, totalCount)
        outState?.putInt(lastEndKey, lastEnd)
    }

    fun onViewStateRestored(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            totalCount = savedInstanceState.getInt(totalCountKey, 0)
            lastEnd = savedInstanceState.getInt(lastEndKey, 0)
        }
    }

    fun getAllTasksObservable(): Observable<RealmResults<Task>> =
            toDosRepository.getAllTasksObservable()

    fun getModifiedTasksObservable(): Observable<RealmResults<Task>> =
            toDosRepository.getModifiedTasksObservable()

    private fun fetchAndStoreToDos(start: Int, limit: Int): Observable<ArrayList<Task>> {
        return toDoApiService.getToDos(start, limit)
                .map { response ->
                    handleResponse(response, start, limit)
                }.doOnNext { list -> toDosRepository.copyOrUpdateTasks(list) }
    }

    private fun handleResponse(response: Response<List<Task>>, start: Int, limit: Int): ArrayList<Task> {
        if (response.isSuccessful) {
            lastEnd = start + limit
            with(response) {
                totalCount = headers()?.get(totalCountHeader)!!.toInt()
                return ArrayList(body())
            }
        }
        return ArrayList()
    }

    fun fetchAndStoreInitialSet(limit: Int): Observable<ArrayList<Task>> {
        return if (lastEnd == 0) fetchAndStoreToDos(lastEnd, limit) else Observable.empty<ArrayList<Task>>()
    }

    fun fetchAndStoreNextSet(limit: Int) = fetchAndStoreToDos(lastEnd, limit)
}