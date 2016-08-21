package com.kulikowski.todos.view

import android.support.v7.widget.RecyclerView
import com.kulikowski.githubsearchclient.view.View
import com.kulikowski.todos.model.Task
import java.util.*

interface TasksListView : View {
    fun bindToDosToList(toDos: ArrayList<Task>)
    fun bindScrollListener(scrollListener: RecyclerView.OnScrollListener)
    fun removeScrollListener(scrollListener: RecyclerView.OnScrollListener)
    fun showLoadingMore()
    fun hideLoadingMore()
    fun startEditTaskView(taskId: Int)
    fun showList()
    fun showEmpty()
    fun showLoading()
    fun showError(message: String)
    fun showError(stringResId: Int)
}