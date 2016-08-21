package com.kulikowski.todos.network

import com.kulikowski.todos.R
import retrofit2.adapter.rxjava.HttpException
import java.net.UnknownHostException

class NetworkErrorsHandler(private val generalErrorAction: (Int) -> Unit,
                           private val customErrorMessage: (String) -> Unit) {
    fun handleError(throwable: Throwable) {
        if (throwable is HttpException) {
            val response = throwable.response()
            val message = response.message()
            val errorBody = response.errorBody()
            //TODO parse body
            customErrorMessage(message)
        } else if (throwable is UnknownHostException) {
            generalErrorAction(R.string.connection_error)
        } else {
            customErrorMessage(throwable.message!!)
        }
    }
}