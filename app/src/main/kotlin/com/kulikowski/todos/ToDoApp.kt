package com.kulikowski.todos

import android.app.Application
import com.kulikowski.todos.di.component.AppComponent
import com.kulikowski.todos.di.component.DaggerAppComponent
import com.kulikowski.todos.di.module.DatabaseModule
import com.kulikowski.todos.di.module.NetworkModule

open class ToDoApp : Application() {
    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        setupAppComponent()
    }
    open fun setupAppComponent() {
        appComponent = DaggerAppComponent
                .builder()
                .networkModule(NetworkModule(BuildConfig.API_BASE_URL))
                .databaseModule(DatabaseModule(this))
                .build()
    }
}