package com.kulikowski.todos.di.module

import android.content.Context
import com.kulikowski.todos.database.RealmProvider
import com.kulikowski.todos.database.ToDosRepository
import dagger.Module
import dagger.Provides
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Singleton

@Module
class DatabaseModule constructor(context: Context) {
    init {
        val realmConfiguration =
                RealmConfiguration.Builder(context).deleteRealmIfMigrationNeeded().build()
        Realm.setDefaultConfiguration(realmConfiguration)
    }


    @Provides
    @Singleton
    fun provideToDosRepository(realmProvider: RealmProvider) = ToDosRepository(realmProvider)
}