package com.shopverse.android.presentation.architecture

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

abstract class BaseViewModel<ViewEffect> : ViewModel() {

    private val mutableAlertFlow by lazy { MutableSharedFlow<Alert>() }
    val alertFlow by lazy { mutableAlertFlow.asSharedFlow() }

    private val mutableEffectFlow by lazy { MutableSharedFlow<ViewEffect>() }
    val effectFlow by lazy { mutableEffectFlow.asSharedFlow() }

    protected suspend fun sendEffect(effect: ViewEffect) = mutableEffectFlow.emit(effect)

    protected suspend fun sendAlert(alert: Alert) = mutableAlertFlow.emit(alert)
}