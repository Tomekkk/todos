package com.kulikowski.todos.network

import com.kulikowski.todos.model.Task
import retrofit2.Response
import retrofit2.http.*
import rx.Observable


interface ToDoApiService {

    @GET("todos")
    fun getToDos(@Query("_start") start: Int, @Query("_limit") limit: Int): Observable<Response<List<Task>>>

    @PUT("todos/{id}")
    fun updateToDo(@Path("id") taskId: Int, @Body task: Task) : Observable<Response<Task>>

}