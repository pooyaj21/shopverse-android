package com.shopverse.android.presentation

import com.shopverse.android.core.stage.AppStageStore
import com.shopverse.android.core.stage.AppStageStoreImpl
import com.shopverse.android.presentation.screen.onboarding.OnboardingViewModel
import com.shopverse.android.presentation.screen.splash.SplashViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appDiModule = module {
    single<AppStageStore> { AppStageStoreImpl(sharedPref = get()) }

    viewModel { SplashViewModel(appStageStore = get()) }
    viewModel { OnboardingViewModel(appStageStore = get()) }
}
