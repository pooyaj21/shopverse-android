package com.shopverse.android.presentation.screen.productDetail

import android.os.Parcelable
import com.shopverse.android.presentation.ui.ScreenArgument
import com.shopverse.android.presentation.ui.Source
import com.shopverse.core.model.Product
import kotlinx.parcelize.Parcelize

@Parcelize
class ProductDetailScreenArgs(
    override val source: Source,
    override val requirements: Requirements,
) : ScreenArgument<ProductDetailScreenArgs.Requirements> {

    @Parcelize
    data class Requirements(
        val product: Product,
    ) : Parcelable
}
