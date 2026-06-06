package com.shopverse.android.presentation

import com.shopverse.android.BuildConfig
import com.shopverse.android.core.cart.CartManager
import com.shopverse.android.core.stage.AppStageStore
import com.shopverse.android.core.stage.AppStageStoreImpl
import com.shopverse.android.presentation.feature.deepLink.DeepLinkLauncher
import com.shopverse.android.presentation.screen.account.AccountViewModel
import com.shopverse.android.presentation.screen.auth.AuthBottomSheetViewModel
import com.shopverse.android.presentation.screen.cart.CartViewModel
import com.shopverse.android.presentation.screen.home.HomeViewModel
import com.shopverse.android.presentation.screen.navigator.NavigatorViewModel
import com.shopverse.android.presentation.screen.onboarding.OnboardingViewModel
import com.shopverse.android.presentation.screen.orderDetail.OrderDetailViewModel
import com.shopverse.android.presentation.screen.orders.OrdersViewModel
import com.shopverse.android.presentation.screen.productDetail.ProductDetailViewModel
import com.shopverse.android.presentation.screen.profile.ProfileViewModel
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
        DeepLinkLauncher(
            appStageStore = get(),
            getSavedProfileUseCase = get()
        )
    }

    single {
        CartManager(
            selectAllProductInCart = get(),
            insertOrUpdateProductToCart = get(),
            deleteProductFromCart = get(),
            deleteAllProductInCart = get(),
        )
    }

    viewModel { MainViewModel() }
    viewModel { SplashViewModel(appStageStore = get()) }
    viewModel { OnboardingViewModel(appStageStore = get()) }
    viewModel { NavigatorViewModel() }
    viewModel { HomeViewModel(getProducts = get(), cartManager = get()) }
    viewModel { CartViewModel(cartManager = get(), submitOrderUseCase = get()) }
    viewModel { OrdersViewModel(getOrders = get()) }
    viewModel { params ->
        OrderDetailViewModel(orderId = params.get(), getOrderUseCase = get())
    }
    viewModel {
        AccountViewModel(
            getSavedProfileUseCase = get(),
            fetchProfileUseCase = get(),
            deleteAccountUseCase = get(),
        )
    }
    viewModel { params ->
        ProductDetailViewModel(
            productId = params.get(),
            getProductUseCase = get(),
            cartManager = get(),
        )
    }
    viewModel { AuthBottomSheetViewModel(loginUseCase = get(), signUpUseCase = get()) }
    viewModel {
        ProfileViewModel(
            getSavedProfileUseCase = get(),
            logoutUseCase = get(),
            getThemeModeUseCase = get(),
            setThemeModeUseCase = get(),
            appVersion = BuildConfig.VERSION_NAME,
            appBuildNumber = BuildConfig.VERSION_CODE.toString(),
        )
    }
}
