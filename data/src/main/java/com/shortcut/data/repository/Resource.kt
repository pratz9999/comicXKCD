package com.shortcut.data.repository

import com.shortcut.data.remote.Failure

// A generic class that contains data and status about loading this data.
sealed class Resource<T>(
    val isLoading: Boolean,
    val data: T? = null,
    val exception: Failure? = null
) {
    class SUCCESS<T>(data: T) : Resource<T>(false, data)
    class LOADING<T>(isLoading: Boolean, data: T? = null) : Resource<T>(isLoading, data)
    class ERROR<T>(ex: Failure?) : Resource<T>(false, exception = ex)
}