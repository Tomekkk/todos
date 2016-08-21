package com.kulikowski.todos.widget

import android.view.View


fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun switchExposedView(visibleView: View, vararg hiddenViews: View) {
    visibleView.show()
    hiddenViews.forEach { it.hide() }
}