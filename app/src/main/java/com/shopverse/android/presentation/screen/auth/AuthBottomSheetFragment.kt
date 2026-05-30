package com.shopverse.android.presentation.screen.auth

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

    private var onLoginFailListener: (() -> Unit)? = null
    private var onLoginSuccessListener: (() -> Unit)? = null
    private var authSucceeded: Boolean = false

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

                AuthBottomSheetViewModel.Effect.AuthCompleted -> {
                    authSucceeded = true
                    onLoginSuccessListener?.invoke()
                    dismiss()
                }
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        if (!authSucceeded) onLoginFailListener?.invoke()
        super.onDismiss(dialog)
    }

    companion object {
        fun newInstance(
            onLoginFailListener: () -> Unit,
            onLoginSuccessListener: () -> Unit,
        ): AuthBottomSheetFragment = AuthBottomSheetFragment().apply {
            this.onLoginFailListener = onLoginFailListener
            this.onLoginSuccessListener = onLoginSuccessListener
        }
    }
}
