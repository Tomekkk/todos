package com.kulikowski.todos.view

import com.kulikowski.githubsearchclient.view.View

interface ToDosView : View{
    fun showSnackBarMessage(resId: Int)
    fun showFilterActive()
    fun showFilterInactive()
    fun showError(message: String)
    fun showError(stringResId: Int)
}
