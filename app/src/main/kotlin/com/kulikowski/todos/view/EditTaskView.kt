package com.kulikowski.todos.view

import com.kulikowski.githubsearchclient.view.View
import com.kulikowski.todos.model.Task


interface EditTaskView : View {
    fun showLoading()
    fun bindTask(task: Task)
    fun showContent()
    fun setToolbarTitle(taskId: Int)
    fun getCheckboxState() : Boolean
    fun getEditedText() : String
    fun showSnackBarMessage(resId: Int)
}