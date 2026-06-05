package com.shopverse.android.presentation.architecture

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


abstract class BaseFragmentVM<V : View, VM : BaseViewModel<*>> :
    BaseFragment<V>() {

    protected abstract val viewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.alertFlow
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.CREATED)
            .onEach { alert ->
                Toast.makeText(
                    requireContext(),
                    alert.message,
                    Toast.LENGTH_SHORT,
                ).show()
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    protected inline fun <reified ViewEffect> onEffect(crossinline action: (ViewEffect) -> Unit) {
        viewModel.effectFlow.onEach { effect ->
            if (effect is ViewEffect) action(effect)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
}