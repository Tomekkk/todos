package com.kulikowski.todos.domain

import com.kulikowski.todos.database.ToDosRepository
import com.kulikowski.todos.model.Task

class TaskUseCase(private val toDosRepository: ToDosRepository){
    fun getTask(taskId: Int) = toDosRepository.getTask(taskId)
    fun updateTask(task: Task) = toDosRepository.updateTask(task)
}
