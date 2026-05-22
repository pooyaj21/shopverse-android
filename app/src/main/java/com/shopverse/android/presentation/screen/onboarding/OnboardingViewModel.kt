package com.shopverse.android.presentation.screen.onboarding

import androidx.lifecycle.viewModelScope
import com.shopverse.android.core.stage.AppStage
import com.shopverse.android.core.stage.AppStageStore
import com.shopverse.android.presentation.architecture.BaseViewModel
import kotlinx.coroutines.launch

class OnboardingViewModel(private val appStageStore: AppStageStore) : BaseViewModel<AppStage>() {

    fun goNextStage() {
        viewModelScope.launch {
            sendEffect(appStageStore.stageCompleted(AppStage.ON_BOARDING))
        }
    }
}