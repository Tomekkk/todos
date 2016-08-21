package com.kulikowski.todos

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import butterknife.BindView
import butterknife.ButterKnife
import com.kulikowski.todos.di.component.DaggerToDosComponent
import com.kulikowski.todos.presenter.ToDosPresenter
import com.kulikowski.todos.view.ToDosView
import com.kulikowski.todos.widget.SnackBarWrapper
import javax.inject.Inject

class ToDosActivity : ToDosView, AppCompatActivity() {
    @Inject
    lateinit var toDosPresenter: ToDosPresenter

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.todos_container)
    lateinit var mainContainer: View

    lateinit var snackBarWrapper: SnackBarWrapper
    private lateinit var alertBuilder: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectDependencies()
        setContentView(R.layout.activity_todos)
        ButterKnife.bind(this)
        setUpToolbar()
        setupSnackBarWrapper()
        toDosPresenter.attachView(this)
        toDosPresenter.onStateRestored(savedInstanceState)
        alertBuilder = AlertDialog.Builder(this)
    }

    private fun setupSnackBarWrapper() {
        snackBarWrapper = SnackBarWrapper(mainContainer)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        toDosPresenter.onSaveInstanceState(outState)
    }

    private fun injectDependencies() {
        DaggerToDosComponent.builder().appComponent(ToDoApp.appComponent).build().inject(this)
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_to_dos, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_backup -> {
                toDosPresenter.onBackupAction()
                return true
            }
            R.id.action_filter -> {
                toDosPresenter.onFilterAction()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showSnackBarMessage(resId: Int) {
        snackBarWrapper.showLongSnack(resId)
    }

    override fun onResume() {
        super.onResume()
        toDosPresenter.onStart()
    }

    override fun onPause() {
        super.onPause()
        toDosPresenter.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        toDosPresenter.detachView()
    }

    override fun showFilterActive() {
        supportActionBar?.subtitle = getString(R.string.todos_modified)
    }

    override fun showFilterInactive() {
        supportActionBar?.subtitle = ""
    }

    override fun showError(message: String) {
        alertBuilder.setMessage(message).create().show()
    }

    override fun showError(stringResId: Int) {
        alertBuilder.setMessage(stringResId).create().show()
    }
}
