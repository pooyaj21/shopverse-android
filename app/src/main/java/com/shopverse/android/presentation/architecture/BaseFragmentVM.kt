package com.shopverse.android.presentation.architecture

import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


abstract class BaseFragmentVM<V : View, VM : BaseViewModel<*>> :
    BaseFragment<V>() {

    protected abstract val viewModel: VM

    protected inline fun <reified ViewEffect> onEffect(crossinline action: (ViewEffect) -> Unit) {
        viewModel.effectFlow.onEach { effect ->
            if (effect is ViewEffect) action(effect)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}