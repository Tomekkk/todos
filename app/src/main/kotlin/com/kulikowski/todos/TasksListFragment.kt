package com.kulikowski.todos

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.kulikowski.githubsearchclient.adapter.TasksListAdapter
import com.kulikowski.todos.di.component.DaggerTasksListComponent
import com.kulikowski.todos.model.Task
import com.kulikowski.todos.presenter.EditTaskPresenter
import com.kulikowski.todos.presenter.TasksListPresenter
import com.kulikowski.todos.view.TasksListView
import com.kulikowski.todos.widget.switchExposedView
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration
import java.util.*
import javax.inject.Inject

class TasksListFragment : TasksListView, Fragment() {
    @Inject
    lateinit var toDosPresenter: TasksListPresenter
    @BindView(R.id.list)
    lateinit var list: RecyclerView
    @BindView(R.id.empty)
    lateinit var empty: TextView
    @BindView(R.id.progress)
    lateinit var progress: ProgressBar
    private lateinit var alertBuilder: AlertDialog.Builder
    private var toDosAdapter: TasksListAdapter? = null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_to_dos, container, false)
        ButterKnife.bind(this, view)
        setupRecyclerView()
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        injectDependencies()
        alertBuilder = AlertDialog.Builder(activity)
        toDosPresenter.attachView(this)
    }

    fun injectDependencies() {
        DaggerTasksListComponent.builder().appComponent(ToDoApp.appComponent).build().inject(this)
    }

    override fun onStart() {
        super.onStart()
        toDosPresenter.onStart()
    }

    override fun onStop() {
        super.onStop()
        toDosPresenter.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        toDosPresenter.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        toDosPresenter.onStateRestored(savedInstanceState)
    }

    override fun onDetach() {
        super.onDetach()
        toDosPresenter.detachView()
    }

    private fun setupRecyclerView() {
        list.layoutManager = LinearLayoutManager(activity)
        list.addItemDecoration(HorizontalDividerItemDecoration.Builder(activity).build())
    }
    override fun bindToDosToList(toDos: ArrayList<Task>) {
        if (toDosAdapter == null) {
            toDosAdapter = TasksListAdapter(toDos, {
                it ->
                toDosPresenter.onItemClick(it)

            })
            list.adapter = toDosAdapter
        } else {
            toDosAdapter!!.todos = toDos
        }
        toDosAdapter!!.notifyDataSetChanged()
    }

    override fun bindScrollListener(scrollListener: RecyclerView.OnScrollListener) {
        list.addOnScrollListener(scrollListener)
    }

    override fun removeScrollListener(scrollListener: RecyclerView.OnScrollListener) {
        list.removeOnScrollListener(scrollListener)
    }

    override fun showLoadingMore() {
        toDosAdapter!!.setLoadingMore()
    }

    override fun hideLoadingMore() {
        toDosAdapter!!.setNotLoadingMore()
    }
    override fun startEditTaskView(taskId: Int) {
        val intent = Intent(activity, EditTaskActivity::class.java)
        intent.putExtra(EditTaskPresenter.TASK_ID_KEY, taskId)
        startActivity(intent)
    }
    override fun showList() {
        switchExposedView(list, empty, progress)
    }

    override fun showEmpty() {
        switchExposedView(empty, list, progress)
    }

    override fun showLoading() {
        switchExposedView(progress, empty, list)
    }

    override fun showError(stringResId: Int) {
        alertBuilder.setMessage(stringResId).create().show()
    }

    override fun showError(message: String) {
        alertBuilder.setMessage(message).create().show()
    }
}