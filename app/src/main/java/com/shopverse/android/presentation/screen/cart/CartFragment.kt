package com.shopverse.android.presentation.screen.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.shopverse.android.core.extension.notImplementedYet
import com.shopverse.android.core.stage.AppStage
import com.shopverse.android.presentation.architecture.BaseFragmentVMState
import com.shopverse.android.presentation.ui.Source
import com.shopverse.android.presentation.ui.navigateAndClearStack
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
        onPlaceOrderClickListener = { ensureUserLogin { viewModel.placeOrder() } },
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onEffect<CartEffect> { effect ->
            when (effect) {
                is CartEffect.OrderPlaced -> {
                    navigateAndClearStack(AppStage.ESTABLISHED, Source.Orders)
                    notImplementedYet()
                }

                is CartEffect.ShowMessage -> Toast.makeText(
                    requireContext(),
                    effect.message,
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }
}
