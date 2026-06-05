package com.shopverse.android.presentation.screen.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.shopverse.android.core.extension.notImplementedYet
import com.shopverse.android.presentation.architecture.BaseFragmentVMState
import com.shopverse.android.presentation.screen.profile.core.ProfileUiModel
import com.shopverse.android.presentation.ui.Source
import com.shopverse.android.presentation.ui.navigateToAccount
import com.shopverse.android.presentation.ui.navigateToOrders
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragmentVMState<ProfileView, ProfileUiModel, ProfileViewModel>() {

    override val currentSource: Source = Source.Profile

    override val viewModel: ProfileViewModel by viewModel()

    override fun onCreateRootView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): ProfileView = ProfileView(
        context = requireContext(),
        onNavigatableClickListener = { item -> handleNavigatable(item) },
        onSimpleClickListener = { item -> handleSimple(item) },
        onEditableClickListener = { item -> handleEditable(item) },
        onTogglableChangeListener = { item -> handleTogglable(item) },
    )

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }

    private fun handleNavigatable(item: ProfileUiModel.Item.Navigatable) {
        when (item) {
            ProfileUiModel.Item.Navigatable.Profile -> ensureUserLogin { navigateToAccount(Source.Profile) }
            ProfileUiModel.Item.Navigatable.Orders -> ensureUserLogin { navigateToOrders(Source.Profile) }
        }
    }

    private fun handleSimple(item: ProfileUiModel.Item.Simple) {
        when (item) {
            ProfileUiModel.Item.Simple.Login -> ensureUserLogin { viewModel.refresh() }
            ProfileUiModel.Item.Simple.Logout -> viewModel.logout()
        }
    }

    private fun handleEditable(item: ProfileUiModel.Item.Editable) {
        when (item) {
            ProfileUiModel.Item.Editable.Language -> notImplementedYet()
        }
    }

    private fun handleTogglable(item: ProfileUiModel.Item.Togglable) {
        when (item) {
            is ProfileUiModel.Item.Togglable.DarkMode -> notImplementedYet()
        }
    }
}
