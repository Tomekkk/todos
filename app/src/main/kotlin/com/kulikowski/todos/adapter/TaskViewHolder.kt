package com.kulikowski.githubsearchclient.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.kulikowski.todos.model.Task
import kotlinx.android.synthetic.main.list_item.view.*

class TaskViewHolder(view: View, val itemClick: (Task) -> Unit) : RecyclerView.ViewHolder(view) {
    fun bindItem(task: Task) {
        with(task) {
            itemView.setOnClickListener { itemClick(this) }
            itemView.item_id.text = id.toString()
            itemView.title.text = title
            itemView.completed.text = completed.toString()
            itemView.not_synced.visibility = if (task.modified != null && task.modified!!) View.VISIBLE else View.GONE
        }
    }
}