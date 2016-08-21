package com.kulikowski.todos.widget

import android.support.design.widget.Snackbar
import android.view.View


class SnackBarWrapper(val view: View) {

    fun showLongSnack(resId: Int){
        Snackbar.make(view, resId, Snackbar.LENGTH_LONG).show()
    }

    fun showLongSnack(charSequence: CharSequence){
        Snackbar.make(view, charSequence, Snackbar.LENGTH_LONG).show()
    }
}