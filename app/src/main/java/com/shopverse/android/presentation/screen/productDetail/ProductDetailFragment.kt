package com.shopverse.android.presentation.screen.productDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.shopverse.android.presentation.architecture.BaseFragmentVMState
import com.shopverse.android.presentation.screen.navigator.NavigatorScreenArgs
import com.shopverse.android.presentation.screen.navigator.NavigatorView
import com.shopverse.android.presentation.ui.Source
import com.shopverse.android.presentation.ui.navigateToNavigator
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ProductDetailFragment :
    BaseFragmentVMState<ProductDetailView, ProductDetailUiModel, ProductDetailViewModel>() {

    override val currentSource: Source = Source.ProductDetail

    private val args: ProductDetailFragmentArgs by navArgs()

    override val viewModel: ProductDetailViewModel by viewModel {
        parametersOf(args.screenArgs.requirements.productId)
    }

    override fun onCreateRootView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): ProductDetailView = ProductDetailView(
        context = requireContext(),
        onRetryClickListener = { viewModel.loadProduct() },
        onAddToCartClickListener = { viewModel.addToCart() },
        onGoToCartClickListener = {
            navigateToNavigator(
                args = NavigatorScreenArgs(
                    source = currentSource,
                    requirements = NavigatorScreenArgs.Requirements(
                        selectTabTag = NavigatorView.TAB_CART,
                        withBackStack = true,
                    )
                )
            )
        },
    )
}
