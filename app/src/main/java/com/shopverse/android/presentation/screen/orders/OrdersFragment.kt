package com.shopverse.android.presentation.screen.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.shopverse.android.core.extension.notImplementedYet
import com.shopverse.android.presentation.architecture.BaseFragmentVMState
import com.shopverse.android.presentation.ui.Source
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrdersFragment : BaseFragmentVMState<OrdersView, OrdersUiModel, OrdersViewModel>() {

    override val currentSource: Source = Source.Orders

    override val viewModel: OrdersViewModel by viewModel()

    override fun onCreateRootView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): OrdersView = OrdersView(
        context = requireContext(),
        onOrderClick = { notImplementedYet() },
        onLoadMore = { viewModel.loadMore() },
        onRetryClickListener = { viewModel.refresh() },
    )
}
