package com.shopverse.android.presentation.screen.navigator

import com.shopverse.android.presentation.architecture.BaseViewModel
import com.shopverse.android.presentation.component.TabBarView
import java.util.Stack

class NavigatorViewModel : BaseViewModel<Nothing>() {
    var tabBarBackStack: Stack<TabBarView.Tab>? = null
}