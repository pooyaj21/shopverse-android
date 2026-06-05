package com.shopverse.android.presentation.screen.productDetail

import android.os.Parcelable
import com.shopverse.android.presentation.ui.ScreenArgument
import com.shopverse.android.presentation.ui.Source
import kotlinx.parcelize.Parcelize

@Parcelize
class ProductDetailScreenArgs(
    override val source: Source,
    override val requirements: Requirements,
) : ScreenArgument<ProductDetailScreenArgs.Requirements> {

    @Parcelize
    data class Requirements(
        val productId: String,
    ) : Parcelable
}
