package com.kulikowski.todos

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.kulikowski.todos.di.component.DaggerEditComponent
import com.kulikowski.todos.model.Task
import com.kulikowski.todos.presenter.EditTaskPresenter
import com.kulikowski.todos.view.EditTaskView
import com.kulikowski.todos.widget.SnackBarWrapper
import com.kulikowski.todos.widget.switchExposedView
import javax.inject.Inject

class EditTaskActivity : EditTaskView, AppCompatActivity() {
    @Inject
    lateinit var editTaskPresenter: EditTaskPresenter

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar
    @BindView(R.id.task_title)
    lateinit var titleTextView: TextView
    @BindView(R.id.task_completed)
    lateinit var completedCheckBox: CheckBox
    @BindView(R.id.progress)
    lateinit var progress: ProgressBar
    @BindView(R.id.task_content)
    lateinit var taskContent: LinearLayout
    @BindView(R.id.edit_container)
    lateinit var mainContainer: View

    lateinit var toolbarTitle: String
    lateinit var snackBarWrapper: SnackBarWrapper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
        setContentView(R.layout.activity_edit_task)
        ButterKnife.bind(this)
        setUpSnackBarWrapper(mainContainer)
        setUpToolbar()
        setUpPresenter(intent, savedInstanceState)
    }

    private fun setUpPresenter(intent: Intent, savedInstanceState: Bundle?) {
        editTaskPresenter.attachView(this)
        editTaskPresenter.onCreate(intent, savedInstanceState)
    }

    private fun setUpToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setUpSnackBarWrapper(mainContainer: View){
        snackBarWrapper = SnackBarWrapper(mainContainer)
    }

    override fun setToolbarTitle(taskId: Int) {
        toolbarTitle = getString(R.string.edit_toolbar_title, taskId)
        supportActionBar?.title = toolbarTitle
    }

    fun injectDependencies() {
        DaggerEditComponent.builder().appComponent(ToDoApp.appComponent).build().inject(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit_task, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_save -> {
                editTaskPresenter.onSave()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun bindTask(task: Task) {
        titleTextView.text = task.title
        completedCheckBox.isChecked = task.completed!!
    }

    override fun showLoading() {
        switchExposedView(progress, taskContent)
    }

    override fun showContent() {
        switchExposedView(taskContent, progress)
    }

    override fun onDestroy() {
        super.onDestroy()
        editTaskPresenter.detachView()
    }

    override fun getCheckboxState(): Boolean = completedCheckBox.isChecked

    override fun getEditedText(): String = titleTextView.text.toString()

    override fun showSnackBarMessage(resId: Int) {
        snackBarWrapper.showLongSnack(resId)
    }
}
