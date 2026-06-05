package com.shopverse.android.presentation.screen.orderDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.shopverse.android.presentation.architecture.BaseFragmentVMState
import com.shopverse.android.presentation.screen.productDetail.ProductDetailScreenArgs
import com.shopverse.android.presentation.ui.Source
import com.shopverse.android.presentation.ui.navigateToProductDetail
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class OrderDetailFragment :
    BaseFragmentVMState<OrderDetailView, OrderDetailUiModel, OrderDetailViewModel>() {

    override val currentSource: Source = Source.OrderDetail

    private val args: OrderDetailFragmentArgs by navArgs()

    override val viewModel: OrderDetailViewModel by viewModel {
        parametersOf(args.screenArgs.requirements.orderId)
    }

    override fun onCreateRootView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): OrderDetailView = OrderDetailView(
        context = requireContext(),
        onRetryClickListener = { viewModel.loadOrder() },
        onItemClickListener = { productId ->
            navigateToProductDetail(
                args = ProductDetailScreenArgs(
                    source = currentSource,
                    requirements = ProductDetailScreenArgs.Requirements(
                        productId = productId
                    )
                )
            )
        },
    )
}
