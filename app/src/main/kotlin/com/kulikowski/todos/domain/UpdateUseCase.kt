package com.kulikowski.todos.domain

import com.kulikowski.todos.database.ToDosRepository
import com.kulikowski.todos.model.Task
import com.kulikowski.todos.network.ToDoApiService
import retrofit2.Response
import rx.Observable

class UpdateUseCase(private val toDoApiService: ToDoApiService,
                    private val toDosRepository: ToDosRepository) {

    fun backupModifiedTasks(): Observable<Boolean> {
        return toDosRepository.getModifiedTasks()
                .flatMap { tasks ->
                    sendTasksAndMergeResults(tasks)
                }.map { response ->
                    updateRepositoryTask(response)
                }.reduce(true, {result1: Boolean?, result2: Boolean? ->
                    reduceUpdateResults(result1, result2)
                })
    }

    private fun reduceUpdateResults(result1: Boolean?, result2: Boolean?): Boolean {
        if (result1 == null || result2 == null) return false
        return result1 && result2
    }

    private fun sendTasksAndMergeResults(tasks: List<Task>): Observable<Response<Task>> {
        val updateObservables: List<Observable<Response<Task>>> = tasks.map { task ->
            toDoApiService.updateToDo(task.id!!, task)
        }
        return Observable.merge(updateObservables)
    }

    private fun updateRepositoryTask(response: Response<Task>): Boolean {
        if (response.isSuccessful) {
            val task = response.body()
            toDosRepository.updateTask(task)
            return true
        }
        return false
    }
}