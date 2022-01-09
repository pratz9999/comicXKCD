package com.shortcut.components.dashboard.base

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.shortcut.data.remote.Failure
import com.shortcut.xkcd.R
import com.shortcut.components.dashboard.common.SnackbarActivty
import kotlinx.coroutines.flow.collect

abstract class BaseViewModelFragment<
        ViewModelType : BaseViewModel,
        VB : ViewBinding,
        > : BaseFragment<VB>() {

    lateinit var viewModel: ViewModelType
    abstract fun getInjectViewModel(): ViewModelType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = getInjectViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
    }

    open fun initObservers() {

        lifecycleScope.launchWhenResumed {
            viewModel.errorObserver.collect {
                hideLoading()
                if (it is Failure.TimeoutException) {
                    showNoConnectionError()

                } else {
                    showError(it.msg ?: getString(R.string.general_error_msg))
                }

            }
        }

    }

    private fun showNoConnectionError() {
        (activity as? SnackbarActivty)?.getSnackbar(getString(R.string.connection_error_msg))?.apply {
            setAction(R.string.snack_bar_retry) { onRetry() }
            show()
        }
    }

    abstract fun onRetry()

}
