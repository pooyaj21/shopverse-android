package com.shopverse.core.data.di

import com.shopverse.core.data.auth.AuthRepository
import com.shopverse.core.data.auth.AuthRepositoryImpl
import com.shopverse.core.data.auth.SessionTokenStoreImpl
import com.shopverse.core.data.cart.CartRepository
import com.shopverse.core.data.cart.CartRepositoryImpl
import com.shopverse.core.data.cart.db.ShopVerseDatabase
import com.shopverse.core.data.order.OrderRepository
import com.shopverse.core.data.order.OrderRepositoryImpl
import com.shopverse.core.data.product.ProductRepository
import com.shopverse.core.data.product.ProductRepositoryImpl
import com.shopverse.core.preferences.di.preferencesDiModule
import com.shopverse.core.service.di.serviceDiModule
import com.shopverse.core.service.supabase.SessionTokenStore
import com.shopverse.core.shared.DefaultDispatcherProvider
import com.shopverse.core.shared.DispatcherProvider
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataDiModule = module {
    includes(serviceDiModule, preferencesDiModule)

    single<DispatcherProvider> { DefaultDispatcherProvider() }

    single<ProductRepository> { ProductRepositoryImpl(productService = get()) }

    single<SessionTokenStore> { SessionTokenStoreImpl(sharedPref = get()) }

    single<AuthRepository> {
        AuthRepositoryImpl(authService = get(), sharedPref = get(), tokenStore = get())
    }

    single<OrderRepository> {
        OrderRepositoryImpl(orderService = get(), authRepository = get())
    }

    single { ShopVerseDatabase.build(androidContext()) }
    single { get<ShopVerseDatabase>().cartDao() }
    single<CartRepository> { CartRepositoryImpl(dao = get()) }
}
