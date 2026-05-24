package com.shopverse.android.presentation.screen.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.shopverse.android.core.extension.notImplementedYet
import com.shopverse.android.presentation.architecture.BaseFragmentVMState
import com.shopverse.android.presentation.ui.Source
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class CartFragment : BaseFragmentVMState<CartView, CartUiModel, CartViewModel>() {

    override val currentSource: Source = Source.Cart

    override val viewModel: CartViewModel by activityViewModel()

    override fun onCreateRootView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): CartView = CartView(
        context = requireContext(),
        onRemoveClickListener = { item -> viewModel.removeFromCart(item) },
        onPlaceOrderClickListener = { notImplementedYet() },
    )
}
