package com.shortcut.components.dashboard.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.shortcut.utils.extensions.hideKeyboard
import com.shortcut.utils.extensions.toast

abstract class BaseFragment<VB : ViewBinding> : Fragment()/*, LifecycleOwner*/ {


    private var _binding: VB? = null
    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB
    protected val binding: VB
        get() = _binding as VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        return requireNotNull(_binding).root
    }

    override fun onStop() {
        super.onStop()
        hideLoading()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideKeyboard()
        initViews()
    }

    abstract fun initViews()

    open fun showLoading() {

    }

    open fun hideLoading() {

    }

    open fun showError(@StringRes stringId: Int) {
        showError(getString(stringId))
    }

    open fun showError(errorString: String) {
        context?.toast(errorString)
    }

    open fun showMessage(@StringRes stringId: Int) {
        showMessage(getString(stringId))
    }

    open fun showMessage(errorString: String) {
        context?.toast(errorString)
    }

}
