package com.shopverse.core.domain.di

import com.shopverse.core.data.di.dataDiModule
import com.shopverse.core.domain.product.GetProductsUseCase
import org.koin.dsl.module

val domainDiModule = module {
    includes(dataDiModule)

    factory { GetProductsUseCase(productRepository = get()) }
}
