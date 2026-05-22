package com.shopverse.android.presentation.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.shopverse.android.presentation.architecture.BaseFragmentVMState
import com.shopverse.android.presentation.ui.Source
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragmentVMState<HomeView, HomeUiModel, HomeViewModel>() {

    override val currentSource: Source = Source.Home

    override val viewModel: HomeViewModel by viewModel()

    override fun onCreateRootView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): HomeView = HomeView(
        context = requireContext(),
        onLoadMore = { viewModel.loadMore() },
        onRetryClickListener = { viewModel.refresh() },
    )
}
