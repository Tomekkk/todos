package com.kulikowski.todos.di.component

import com.kulikowski.todos.database.ToDosRepository
import com.kulikowski.todos.di.module.DatabaseModule
import com.kulikowski.todos.di.module.NetworkModule
import com.kulikowski.todos.network.ToDoApiService
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(NetworkModule::class, DatabaseModule::class))
interface AppComponent {
    fun toDosApiService(): ToDoApiService
    fun toDosRepository(): ToDosRepository
}