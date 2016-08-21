package com.kulikowski.todos.di.component

import com.kulikowski.githubsearchclient.di.scope.PerActivity
import com.kulikowski.todos.TasksListFragment
import com.kulikowski.todos.di.module.TasksListModule
import dagger.Component

@Component(dependencies = arrayOf(AppComponent::class),
        modules = arrayOf(TasksListModule::class))
@PerActivity
interface TasksListComponent {
    fun inject(toDosListFragment: TasksListFragment)
}