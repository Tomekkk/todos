package com.kulikowski.todos.presenter

import android.os.Bundle
import com.kulikowski.githubsearchclient.rx.SchedulersTransformer
import com.kulikowski.todos.domain.TasksUseCase
import com.kulikowski.todos.event.FilterActiveEvent
import com.kulikowski.todos.event.FilterEvent
import com.kulikowski.todos.listener.LoadMoreListener
import com.kulikowski.todos.model.Task
import com.kulikowski.todos.network.NetworkErrorsHandler
import com.kulikowski.todos.view.TasksListView
import io.realm.RealmResults
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import rx.android.schedulers.AndroidSchedulers
import java.util.*

class TasksListPresenter(private val tasksUseCase: TasksUseCase) : BaseRxPresenter<TasksListView>() {
    private val filterActiveKey = "filter-active"
    private val itemsPerPage: Int = 10
    private val loadMoreThreshold = 1
    private var loadingMore = false

    private val bus = EventBus.getDefault()

    private var filterActive: Boolean = false
    private var currentRealmResults: RealmResults<Task>? = null
    private val loadMoreListener = LoadMoreListener(loadMoreThreshold, {
        if (!filterActive) {
            fetchMore()
        }
    })
    private val networkErrorsHandler: NetworkErrorsHandler = NetworkErrorsHandler(
            { errorResId -> view.showError(errorResId) },
            { errorMessage -> view.showError(errorMessage) })

    fun onStart() {
        bus.register(this)
        getTasksFromDatabase(filterActive)
        bindLoadMoreListener()
    }

    fun onStop() {
        bus.unregister(this)
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onFilterEvent(filterEvent: FilterEvent) {
        filterActive = !filterActive
        getTasksFromDatabase(filterActive)
        bus.post(FilterActiveEvent(filterActive))
    }

    private fun bindLoadMoreListener() {
        view.bindScrollListener(loadMoreListener)
    }

    override fun detachView() {
        super.detachView()
        view.removeScrollListener(loadMoreListener)
    }

    private fun getTasksFromDatabase(modifiedOnly: Boolean) {
        currentRealmResults?.removeChangeListeners()
        unsubscribeAndInitCompositeSubscription()
        view.showLoading()
        val observable = if (modifiedOnly) tasksUseCase.getModifiedTasksObservable()
        else tasksUseCase.getAllTasksObservable()
        val subscription = observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { realmResults ->
                    bindDatabaseResults(realmResults)
                    fetchInitialSet(realmResults.size)
                }
        compositeSubscription!!.add(subscription)
    }

    private fun bindDatabaseResults(realmResults: RealmResults<Task>) {
        currentRealmResults = realmResults
        bindDataAndShowResult(currentRealmResults!!)
        currentRealmResults!!.addChangeListener { listenerResult ->
            bindDataAndShowResult(listenerResult)
        }
    }

    private fun bindDataAndShowResult(realmResults: RealmResults<Task>) {
        view.bindToDosToList(ArrayList(realmResults))
        if (realmResults.isEmpty()) {
            view.showEmpty()
        } else {
            view.showList()
        }
    }

    private fun fetchInitialSet(databaseObjectsCount: Int) {
        val subscription = tasksUseCase.fetchAndStoreInitialSet(
                if (databaseObjectsCount == 0) itemsPerPage else databaseObjectsCount)
                .compose(SchedulersTransformer.applyIoSchedulers<ArrayList<Task>>())
                .subscribe({

                }, {
                    error -> handleFetchInitialSetError(error)
                })
        compositeSubscription!!.add(subscription)
    }

    private fun handleFetchInitialSetError(error: Throwable) {
        networkErrorsHandler.handleError(error)
    }

    private fun fetchMore() {
        if (!loadingMore && tasksUseCase.hasNext()) {
            loadingMore = true
            view.showLoadingMore()
            val subscription = tasksUseCase.fetchAndStoreNextSet(itemsPerPage)
                    .compose(SchedulersTransformer.applyIoSchedulers<ArrayList<Task>>())
                    .subscribe({
                        handleLoadMoreResults()
                    }, {
                        error ->
                        handleLoadMoreError(error)
                    })
            compositeSubscription!!.add(subscription)
        }
    }

    private fun handleLoadMoreResults() {
        presentLoadingResults()
    }

    private fun handleLoadMoreError(error: Throwable) {
        networkErrorsHandler.handleError(error)
        presentLoadingResults()
    }

    private fun presentLoadingResults() {
        view.hideLoadingMore()
        loadMoreListener.notifyDataLoaded()
        loadingMore = false
    }

    fun onSaveInstanceState(outState: Bundle?) {
        outState?.putBoolean(filterActiveKey, filterActive)
        tasksUseCase.onSaveInstanceState(outState)
    }

    fun onStateRestored(savedInstanceState: Bundle?) {
        filterActive = savedInstanceState?.getBoolean(filterActiveKey, false) ?: false
        tasksUseCase.onViewStateRestored(savedInstanceState)
    }

    fun onItemClick(task: Task) {
        view.startEditTaskView(task.id!!)
    }

}