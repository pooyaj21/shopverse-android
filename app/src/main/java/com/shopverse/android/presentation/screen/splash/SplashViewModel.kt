package com.shopverse.android.presentation.screen.splash

import androidx.lifecycle.viewModelScope
import com.shopverse.android.core.stage.AppStage
import com.shopverse.android.core.stage.AppStageStore
import com.shopverse.android.presentation.architecture.BaseViewModel
import kotlinx.coroutines.launch

class SplashViewModel(private val appStageStore: AppStageStore) : BaseViewModel<AppStage>() {

    fun splashEnded() {
        viewModelScope.launch {
            sendEffect(appStageStore.getCurrentStage())
        }
    }
}