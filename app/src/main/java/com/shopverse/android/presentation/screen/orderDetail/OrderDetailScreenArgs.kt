package com.shopverse.android.presentation.screen.orderDetail

import android.os.Parcelable
import com.shopverse.android.presentation.ui.ScreenArgument
import com.shopverse.android.presentation.ui.Source
import kotlinx.parcelize.Parcelize

@Parcelize
class OrderDetailScreenArgs(
    override val source: Source,
    override val requirements: Requirements,
) : ScreenArgument<OrderDetailScreenArgs.Requirements> {

    @Parcelize
    data class Requirements(
        val orderId: String,
    ) : Parcelable
}
