package com.shopverse.android.presentation.screen.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.shopverse.android.core.layout.AppLayout
import com.shopverse.android.presentation.architecture.BaseBottomSheetDialogFragmentVMState
import com.shopverse.android.presentation.ui.Source
import org.koin.androidx.viewmodel.ext.android.viewModel

class AuthBottomSheetFragment : BaseBottomSheetDialogFragmentVMState<
        AuthBottomSheetView,
        AuthBottomSheetUiModel,
        AuthBottomSheetViewModel,
        >() {

    override val currentSource: Source = Source.Profile

    override val viewModel: AuthBottomSheetViewModel by viewModel()

    override fun getDialogHeight(): Int = AppLayout.WRAP

    override fun onCreateContainerView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): AuthBottomSheetView = AuthBottomSheetView(
        context = requireContext(),
        onCloseClickListener = { dismiss() },
        onSwitchModeClickListener = { viewModel.switchMode() },
        onSubmitClickListener = { name, email, password ->
            viewModel.submit(name = name, email = email, password = password)
        },
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onEffect<AuthBottomSheetViewModel.Effect> { effect ->
            when (effect) {
                is AuthBottomSheetViewModel.Effect.ShowMessage -> Toast.makeText(
                    requireContext(),
                    effect.message,
                    Toast.LENGTH_SHORT,
                ).show()
                AuthBottomSheetViewModel.Effect.AuthCompleted -> dismiss()
            }
        }
    }

    companion object {
        private const val TAG = "AuthBottomSheetFragment"

        fun show(fragmentManager: FragmentManager) {
            if (fragmentManager.findFragmentByTag(TAG) != null) return
            AuthBottomSheetFragment().show(fragmentManager, TAG)
        }
    }
}
