package com.kulikowski.todos.presenter

import android.content.Intent
import android.os.Bundle
import com.kulikowski.todos.R
import com.kulikowski.todos.domain.TaskUseCase
import com.kulikowski.todos.model.Task
import com.kulikowski.todos.view.EditTaskView
import rx.android.schedulers.AndroidSchedulers

class EditTaskPresenter(val taskUseCase: TaskUseCase) : BaseRxPresenter<EditTaskView>() {
    companion object {
        const val TASK_ID_KEY: String = "task-id"
    }

    var taskId: Int = -1
    lateinit var loadedTask: Task
    fun onCreate(intent: Intent, saveInstanceState: Bundle?) {
        taskId = intent.getIntExtra(TASK_ID_KEY, -1)
        if (taskId == -1) throw IllegalArgumentException("Passed intent must contain valid task id")
        loadTaskIfInitialCreate(saveInstanceState)
        setToolbarTaskId(taskId)
    }

    private fun loadTaskIfInitialCreate(saveInstanceState: Bundle?) {
        loadTask(taskId, saveInstanceState == null)
    }

    private fun setToolbarTaskId(taskId: Int) {
        view.setToolbarTitle(taskId)
    }

    private fun loadTask(taskId: Int, withBindingToView: Boolean) {
        unsubscribeAndInitCompositeSubscription()
        view.showLoading()
        val subscription = taskUseCase.getTask(taskId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    task ->
                    handleQuerySuccess(task, withBindingToView)
                }, {
                    error ->
                    handleQueryError(error)
                })
        compositeSubscription!!.add(subscription)
    }

    private fun handleQuerySuccess(task: Task, withBindingToView: Boolean) {
        loadedTask = task
        if (withBindingToView) {
            view.bindTask(loadedTask)
        }
        view.showContent()
    }

    private fun handleQueryError(throwable: Throwable) {
        view.showSnackBarMessage(R.string.edit_unable_save)
        //TODO pass throwable to crash report
    }


    fun onSave() {
        loadedTask.title = view.getEditedText()
        loadedTask.completed = view.getCheckboxState()
        loadedTask.modified = true
        taskUseCase.updateTask(loadedTask)
        view.showSnackBarMessage(R.string.edit_saved)
    }
}