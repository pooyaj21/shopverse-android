package com.shopverse.android.presentation.screen.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.shopverse.android.core.color.AppColorProvider
import com.shopverse.android.core.stage.AppStage
import com.shopverse.android.presentation.architecture.BaseFragmentVMState
import com.shopverse.android.presentation.ui.Source
import com.shopverse.android.presentation.ui.navigateAndClearStack
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountFragment : BaseFragmentVMState<AccountView, AccountUiModel, AccountViewModel>() {

    override val currentSource: Source = Source.Account

    override val viewModel: AccountViewModel by viewModel()

    override fun onCreateRootView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): AccountView = AccountView(
        context = requireContext(),
        onDeleteAccountClickListener = { confirmDeleteAccount() },
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onEffect<AccountEffect> { effect ->
            when (effect) {
                is AccountEffect.AccountDeleted -> {
                    navigateAndClearStack(AppStage.ESTABLISHED, currentSource)
                }
            }
        }
    }

    private fun confirmDeleteAccount() {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete account?")
            .setMessage(
                "This permanently deletes your account and order history. " +
                        "This can't be undone."
            )
            .setPositiveButton("Delete") { _, _ -> viewModel.deleteAccount() }
            .setNegativeButton("Cancel", null)
            .show()
            .getButton(AlertDialog.BUTTON_POSITIVE)
            ?.setTextColor(AppColorProvider.discountBadge.value(requireContext()))
    }
}
