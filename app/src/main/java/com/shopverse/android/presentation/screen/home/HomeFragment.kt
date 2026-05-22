package com.shopverse.android.presentation.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.shopverse.android.presentation.architecture.BaseFragment
import com.shopverse.android.presentation.ui.Source

class HomeFragment : BaseFragment<HomeView>() {

    override val currentSource: Source = Source.Home

    override fun onCreateRootView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): HomeView {
        return HomeView(context = requireContext())
    }
}
