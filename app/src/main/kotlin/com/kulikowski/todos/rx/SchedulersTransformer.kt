package com.kulikowski.githubsearchclient.rx

import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

object SchedulersTransformer {
    fun <T> applyIoSchedulers(): Observable.Transformer<T, T> {
        return Observable.Transformer<T, T> {
            observable ->
            observable
                    .unsubscribeOn(Schedulers.io())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }
}