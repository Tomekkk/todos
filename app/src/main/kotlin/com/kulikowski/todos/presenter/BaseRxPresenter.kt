package com.kulikowski.todos.presenter

import com.kulikowski.githubsearchclient.presenter.Presenter
import com.kulikowski.githubsearchclient.view.View
import rx.subscriptions.CompositeSubscription

abstract class BaseRxPresenter<T : View> : Presenter<T> {
    protected var compositeSubscription: CompositeSubscription? = null
    protected lateinit var view: T

    fun initCompositeSubscription() {
        if (compositeSubscription == null || compositeSubscription!!.isUnsubscribed) {
            compositeSubscription = CompositeSubscription()
        }
    }

    fun unsubscribeAndInitCompositeSubscription() {
        compositeSubscription?.unsubscribe()
        initCompositeSubscription()
    }

    override fun attachView(view: T) {
        this.view = view
    }

    override fun detachView() {
        compositeSubscription?.unsubscribe()
    }
}