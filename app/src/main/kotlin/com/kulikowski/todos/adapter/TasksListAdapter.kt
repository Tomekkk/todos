package com.kulikowski.githubsearchclient.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.kulikowski.todos.R
import com.kulikowski.todos.adapter.LoadMoreViewHolder
import com.kulikowski.todos.model.Task
import java.util.*


class TasksListAdapter(var todos: ArrayList<Task>,
                       val itemClick: (Task) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val taskViewType: Int = 0
    val loadMoreViewType: Int = 1
    var loadingMore: Boolean = false

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        if (getItemViewType(position) == taskViewType) {
            (holder as TaskViewHolder).bindItem(todos[position])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            loadMoreViewType -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.list_load_more,
                        parent, false)
                return LoadMoreViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent,
                        false)
                return TaskViewHolder(view, itemClick)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (loadingMore && position == (itemCount - 1)) loadMoreViewType else taskViewType
    }

    override fun getItemCount(): Int = todos.size + if (loadingMore) 1 else 0

    fun setLoadingMore() {
        if (!loadingMore) {
            loadingMore = true
            notifyItemInserted(itemCount - 1)
        }
    }

    fun setNotLoadingMore() {
        if(loadingMore) {
            val last = itemCount - 1
            loadingMore = false
            notifyItemRemoved(last)
        }
    }
}