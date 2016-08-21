package com.kulikowski.todos.presenter

import android.os.Bundle
import com.kulikowski.githubsearchclient.rx.SchedulersTransformer
import com.kulikowski.todos.R
import com.kulikowski.todos.domain.UpdateUseCase
import com.kulikowski.todos.event.FilterActiveEvent
import com.kulikowski.todos.event.FilterEvent
import com.kulikowski.todos.network.NetworkErrorsHandler
import com.kulikowski.todos.view.ToDosView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class ToDosPresenter(private val updateUseCase: UpdateUseCase) : BaseRxPresenter<ToDosView>() {
    private val bus = EventBus.getDefault()
    private val filterActiveKey = "filterActive"
    private var filterActive : Boolean = false
    private val networkErrorsHandler: NetworkErrorsHandler = NetworkErrorsHandler(
            { errorResId -> view.showError(errorResId) },
            { errorMessage -> view.showError(errorMessage) })

    fun onSaveInstanceState(outState: Bundle?) {
        outState?.putBoolean(filterActiveKey, filterActive)
    }

    fun onStateRestored(savedInstanceState: Bundle?) {
        filterActive = savedInstanceState?.getBoolean(filterActiveKey, false) ?: false
    }

    fun onStart(){
        bus.register(this)
        updateFilterState(filterActive)
    }

    fun onStop(){
        bus.unregister(this)
    }

    fun onBackupAction() {
        unsubscribeAndInitCompositeSubscription()
        val subscription = updateUseCase.backupModifiedTasks()
                .compose(SchedulersTransformer.applyIoSchedulers<Boolean>())
                .subscribe({
                    result ->
                    handleBackupResponse(result)
                }, {
                    error ->
                    handleBackupError(error)
                })
        compositeSubscription!!.add(subscription)
    }

    private fun handleBackupResponse(result: Boolean) {
        if (result) {
            view.showSnackBarMessage(R.string.todos_update_completed)
        } else {
            view.showSnackBarMessage(R.string.todos_update_fail)
        }
    }

    private fun handleBackupError(throwable: Throwable) {
        networkErrorsHandler.handleError(throwable)
    }

    fun onFilterAction() {
        bus.post(FilterEvent())
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onFilterEvent(filterEvent: FilterActiveEvent) {
        updateFilterState(filterEvent.active)
    }

    private fun updateFilterState(active: Boolean){
        filterActive = active
        if(active){
            view.showFilterActive()
        } else {
            view.showFilterInactive()
        }
    }
}