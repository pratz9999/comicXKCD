package com.shortcut.components.dashboard.base

import androidx.lifecycle.ViewModel
import com.shortcut.data.remote.Failure
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

abstract class BaseViewModel : ViewModel() {

    val _loading = Channel<Boolean>(Channel.BUFFERED)
    val loading get() = _loading.receiveAsFlow()

    private val _errorObserver = Channel<Failure>(Channel.BUFFERED)
    val errorObserver get() = _errorObserver.receiveAsFlow()

    //I prefer a global handler to be able to handle the common cases like unauthorized user,..
    internal val handler = CoroutineExceptionHandler { _, e ->
        doOnException(e)
    }

    private fun doOnException(e: Throwable) {
        val ex = when (e) {
            is ConnectException,
            is Failure.NetworkException,
            -> {
                Failure.NetworkException(e.message)
            }

            is Failure.ServerException -> {
                Failure.ServerException(e.msg)
            }

            is UnknownHostException,
            is SocketTimeoutException -> {
                Failure.TimeoutException(e.message)
            }

            else -> {
                Failure.UnknownException(e.message)
            }

        }

        _errorObserver.trySend(ex)
    }
}