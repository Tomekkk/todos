package com.kulikowski.todos.di.module

import com.kulikowski.githubsearchclient.di.scope.PerActivity
import com.kulikowski.todos.database.ToDosRepository
import com.kulikowski.todos.domain.TasksUseCase
import com.kulikowski.todos.network.ToDoApiService
import com.kulikowski.todos.presenter.TasksListPresenter
import dagger.Module
import dagger.Provides

@Module
class TasksListModule {

    @Provides
    @PerActivity
    fun provideToDosUseCase(toDoApiService: ToDoApiService, toDosRepository: ToDosRepository) =
            TasksUseCase(toDoApiService, toDosRepository)

    @Provides
    @PerActivity
    fun provideToDosPresenter(tasksUseCase: TasksUseCase) = TasksListPresenter(tasksUseCase)
}