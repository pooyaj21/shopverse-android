package com.shopverse.android.presentation.architecture

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.CallSuper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.shopverse.android.core.extension.TAG
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class BaseFragmentVMState<V : View, Model, VM : BaseViewModelState<Model, *>> :
    BaseFragmentVM<V, VM>() {

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        @Suppress("UNCHECKED_CAST")
        (rootView as? ViewStateRenderer<Model>)?.let { viewRenderer ->
            viewModel.viewStateFlow
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.RESUMED)
                .onEach { viewState ->
                    val tag = this@BaseFragmentVMState.TAG
                    Log.d(tag, "$tag, render lifecycle: $viewState")
                    viewRenderer.render(viewState)
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

}