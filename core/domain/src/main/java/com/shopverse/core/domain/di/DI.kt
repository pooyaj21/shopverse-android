package com.shopverse.core.domain.di

import com.shopverse.core.data.di.dataDiModule
import com.shopverse.core.domain.cart.DeleteAllProductInCartUseCase
import com.shopverse.core.domain.cart.DeleteProductFromCartUseCase
import com.shopverse.core.domain.cart.InsertOrUpdateProductToCartUseCase
import com.shopverse.core.domain.cart.SelectAllProductInCartUseCase
import com.shopverse.core.domain.product.GetProductsUseCase
import org.koin.dsl.module

val domainDiModule = module {
    includes(dataDiModule)

    factory { GetProductsUseCase(productRepository = get()) }

    factory { SelectAllProductInCartUseCase(cartRepository = get()) }
    factory { InsertOrUpdateProductToCartUseCase(cartRepository = get()) }
    factory { DeleteProductFromCartUseCase(cartRepository = get()) }
    factory { DeleteAllProductInCartUseCase(cartRepository = get()) }
}
