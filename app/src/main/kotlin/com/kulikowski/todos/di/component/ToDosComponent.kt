package com.kulikowski.todos.di.component

import com.kulikowski.githubsearchclient.di.scope.PerActivity
import com.kulikowski.todos.ToDosActivity
import com.kulikowski.todos.di.module.ToDosModule
import dagger.Component

@Component(dependencies = arrayOf(AppComponent::class),
        modules = arrayOf(ToDosModule::class))
@PerActivity
interface ToDosComponent {
    fun inject(toDosActivity: ToDosActivity)
}