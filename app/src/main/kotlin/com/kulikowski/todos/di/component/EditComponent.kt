package com.kulikowski.todos.di.component

import com.kulikowski.githubsearchclient.di.scope.PerActivity
import com.kulikowski.todos.EditTaskActivity
import com.kulikowski.todos.di.module.EditTaskModule
import dagger.Component

@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(EditTaskModule::class))
@PerActivity
interface EditComponent {
    fun inject(editTaskActivity: EditTaskActivity)
}