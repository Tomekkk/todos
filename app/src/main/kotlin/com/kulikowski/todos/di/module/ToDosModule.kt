package com.kulikowski.todos.di.module

import com.kulikowski.githubsearchclient.di.scope.PerActivity
import com.kulikowski.todos.database.ToDosRepository
import com.kulikowski.todos.domain.UpdateUseCase
import com.kulikowski.todos.network.ToDoApiService
import com.kulikowski.todos.presenter.ToDosPresenter
import dagger.Module
import dagger.Provides

@Module
class ToDosModule {

    @Provides
    @PerActivity
    fun provideUpdateUseCase(toDoApiService: ToDoApiService, toDosRepository: ToDosRepository) =
            UpdateUseCase(toDoApiService, toDosRepository)

    @Provides
    @PerActivity
    fun provideToDosPresenter(updateUseCase: UpdateUseCase): ToDosPresenter =
            ToDosPresenter(updateUseCase)
}