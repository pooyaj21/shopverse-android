package com.shopverse.android.presentation.screen.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.shopverse.android.presentation.architecture.BaseFragment
import com.shopverse.android.presentation.ui.Source

class CartFragment : BaseFragment<CartView>() {

    override val currentSource: Source = Source.Cart

    override fun onCreateRootView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): CartView = CartView(requireContext())
}
