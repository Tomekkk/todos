package com.kulikowski.todos.model

import com.google.gson.annotations.Expose
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Task(
        @Expose open var userId: Int? = null,
        @Expose @PrimaryKey open var id: Int? = null,
        @Expose open var title: String? = null,
        @Expose open var completed: Boolean? = null,
        open var modified: Boolean? = null
) : RealmObject(){
    companion object {
        const val ID_KEY = "id"
        const val MODIFIED_KEY = "modified"
    }
}