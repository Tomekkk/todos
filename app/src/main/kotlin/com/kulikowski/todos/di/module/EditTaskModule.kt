package com.kulikowski.todos.di.module

import com.kulikowski.githubsearchclient.di.scope.PerActivity
import com.kulikowski.todos.database.ToDosRepository
import com.kulikowski.todos.domain.TaskUseCase
import com.kulikowski.todos.presenter.EditTaskPresenter
import dagger.Module
import dagger.Provides

@Module
class EditTaskModule {

    @Provides
    @PerActivity
    fun provideTaskUseCase(toDosRepository: ToDosRepository) = TaskUseCase(toDosRepository)

    @Provides
    @PerActivity
    fun provideEditTaskPresenter(taskUseCase: TaskUseCase) = EditTaskPresenter(taskUseCase)
}