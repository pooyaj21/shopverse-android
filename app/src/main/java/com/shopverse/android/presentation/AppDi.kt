package com.shopverse.android.presentation

import com.shopverse.android.BuildConfig
import com.shopverse.android.core.cart.CartManager
import com.shopverse.android.core.stage.AppStageStore
import com.shopverse.android.core.stage.AppStageStoreImpl
import com.shopverse.android.presentation.screen.home.HomeViewModel
import com.shopverse.android.presentation.screen.onboarding.OnboardingViewModel
import com.shopverse.android.presentation.screen.splash.SplashViewModel
import com.shopverse.core.service.supabase.SupabaseConfig
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appDiModule = module {
    single<AppStageStore> { AppStageStoreImpl(sharedPref = get()) }

    single {
        SupabaseConfig(
            baseUrl = BuildConfig.SUPABASE_URL,
            anonKey = BuildConfig.SUPABASE_ANON_KEY,
        )
    }

    single {
        CartManager(
            selectAllProductInCart = get(),
            insertOrUpdateProductToCart = get(),
            deleteProductFromCart = get(),
        )
    }

    viewModel { MainViewModel() }
    viewModel { SplashViewModel(appStageStore = get()) }
    viewModel { OnboardingViewModel(appStageStore = get()) }
    viewModel { HomeViewModel(getProducts = get(), cartManager = get()) }
}
