package com.kulikowski.githubsearchclient.presenter

import com.kulikowski.githubsearchclient.view.View

interface Presenter<in T : View> {
    open fun attachView(view: T)
    open fun detachView()
}