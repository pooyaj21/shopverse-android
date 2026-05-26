package com.shopverse.android.presentation.screen.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.shopverse.android.presentation.architecture.BaseFragment
import com.shopverse.android.presentation.ui.Source

class ProfileFragment : BaseFragment<ProfileView>() {

    override val currentSource: Source = Source.Profile

    override fun onCreateRootView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): ProfileView = ProfileView(requireContext())
}
