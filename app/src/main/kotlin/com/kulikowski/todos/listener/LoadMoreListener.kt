package com.kulikowski.todos.listener

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView


class LoadMoreListener(val threshold: Int, val requestLoad: () -> Unit) : RecyclerView.OnScrollListener() {

    private var visibleItemCount = 0
    private var totalItemCount = 0
    private var firstVisibleItem = 0
    private var previousTotal = 0
    private var loading = true

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        visibleItemCount = recyclerView!!.childCount
        totalItemCount = recyclerView.layoutManager.itemCount
        val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition()

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + threshold)) {
            requestLoad.invoke()
            loading = true
        }
    }

    fun notifyDataLoaded() {
        visibleItemCount = 0
        totalItemCount = 0
        firstVisibleItem = 0
        previousTotal = 0
        loading = true
    }
}