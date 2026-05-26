package com.shopverse.android.presentation.screen.navigator

import android.os.Parcelable
import com.shopverse.android.presentation.ui.ScreenArgument
import com.shopverse.android.presentation.ui.Source
import kotlinx.parcelize.Parcelize

@Parcelize
class NavigatorScreenArgs(
    override val source: Source,
    override val requirements: Requirements
) : ScreenArgument<NavigatorScreenArgs.Requirements> {

    @Parcelize
    data class Requirements(
        val selectTabTag: String = NavigatorView.TAB_HOME,
        val withBackStack: Boolean = false,
    ) : Parcelable
}