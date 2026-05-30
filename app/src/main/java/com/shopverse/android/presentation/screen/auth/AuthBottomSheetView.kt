package com.shopverse.android.presentation.screen.auth

import android.annotation.SuppressLint
import android.content.Context
import android.text.InputType
import android.view.Gravity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.isVisible
import com.shopverse.android.core.color.AppColorProvider
import com.shopverse.android.core.drawable.DrawableBuilder
import com.shopverse.android.core.extension.dp
import com.shopverse.android.core.extension.setPadding
import com.shopverse.android.core.extension.setTextColor
import com.shopverse.android.core.extension.setTypography
import com.shopverse.android.core.layout.AppLayout
import com.shopverse.android.core.layout.margin
import com.shopverse.android.core.typography.Typography
import com.shopverse.android.core.ui.AppVerticalLinearLayout
import com.shopverse.android.presentation.architecture.BaseDialogView
import com.shopverse.android.presentation.component.AppButtonView
import com.shopverse.android.presentation.component.AppTextView

@SuppressLint("ViewConstructor")
class AuthBottomSheetView(
    context: Context,
    onCloseClickListener: () -> Unit,
    private val onSwitchModeClickListener: () -> Unit,
    private val onSubmitClickListener: (name: String, email: String, password: String) -> Unit,
) : BaseDialogView.State<AuthBottomSheetUiModel>(
    context = context,
    onCloseClickListener = onCloseClickListener,
    onRetryClickListener = {},
) {

    override val title: String = "Welcome"

    private var currentMode: AuthMode = AuthMode.Login

    private val headlineView = AppTextView(context).apply {
        setTypography(Typography.B20)
        setTextColor(AppColorProvider.black)
    }

    private val subtitleView = AppTextView(context).apply {
        setTypography(Typography.R14)
        setTextColor(AppColorProvider.gray)
    }

    private val nameField = buildField(
        hint = "Full name",
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS,
    )

    private val emailField = buildField(
        hint = "Email",
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
    )

    private val passwordField = buildField(
        hint = "Password",
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD,
    )

    private val submitButton = AppButtonView(context).apply {
        text = "Log in"
        setOnClickListener {
            onSubmitClickListener(
                nameField.text?.toString().orEmpty().trim(),
                emailField.text?.toString().orEmpty().trim(),
                passwordField.text?.toString().orEmpty(),
            )
        }
    }

    private val footerView = AppTextView(context).apply {
        setTypography(Typography.M14)
        setTextColor(AppColorProvider.primaryMain)
        gravity = Gravity.CENTER
        setPadding(vertical = 8.dp)
        text = "Don't have an account? Create one"
        setOnClickListener { onSwitchModeClickListener() }
    }

    private val formContainer = AppVerticalLinearLayout(context).apply {
        setPadding(horizontal = 20.dp, vertical = 8.dp)
        addView(headlineView, AppLayout.Linear.defaultParams())
        addView(
            subtitleView,
            AppLayout.Linear.defaultParams().margin(top = 4.dp, bottom = 16.dp)
        )
        addView(
            nameField,
            AppLayout.Linear.defaultParams().margin(bottom = FIELD_GAP_DP.dp)
        )
        addView(
            emailField,
            AppLayout.Linear.defaultParams().margin(bottom = FIELD_GAP_DP.dp)
        )
        addView(
            passwordField,
            AppLayout.Linear.defaultParams().margin(bottom = 20.dp)
        )
        addView(submitButton, AppLayout.Linear.defaultParams())
        addView(
            footerView,
            AppLayout.Linear.defaultParams().margin(top = 8.dp, bottom = 16.dp)
        )
    }

    init {
        setContent(formContainer)
    }

    override fun renderSuccess(model: AuthBottomSheetUiModel) {
        currentMode = model.mode
        applyMode(model.mode)
        applySubmitting(model.isSubmitting)
    }

    private fun applyMode(mode: AuthMode) {
        when (mode) {
            AuthMode.Login -> {
                headlineView.text = "Welcome back"
                subtitleView.text = "Log in to continue shopping."
                nameField.isVisible = false
                submitButton.text = "Log in"
                footerView.text = "Don't have an account? Create one"
            }
            AuthMode.Register -> {
                headlineView.text = "Create your account"
                subtitleView.text = "Sign up to start shopping."
                nameField.isVisible = true
                submitButton.text = "Create account"
                footerView.text = "Already have an account? Log in"
            }
        }
    }

    private fun applySubmitting(isSubmitting: Boolean) {
        submitButton.isEnabled = !isSubmitting
        submitButton.alpha = if (isSubmitting) 0.6F else 1F
        footerView.isEnabled = !isSubmitting
        nameField.isEnabled = !isSubmitting
        emailField.isEnabled = !isSubmitting
        passwordField.isEnabled = !isSubmitting
    }

    private fun buildField(hint: String, inputType: Int): AppCompatEditText =
        AppCompatEditText(context).apply {
            this.hint = hint
            this.inputType = inputType
            setTypography(Typography.R16)
            setTextColor(AppColorProvider.black)
            setHintTextColor(AppColorProvider.gray.value(context))
            setPadding(horizontal = 14.dp, vertical = 12.dp)
            background = DrawableBuilder(context)
                .solidColor(AppColorProvider.cardSurface)
                .strokeColor(AppColorProvider.buttonOutline)
                .strokeWidth(1.dp)
                .cornerRadius(12.dp)
                .build()
            minHeight = FIELD_HEIGHT_DP.dp
        }

    companion object {
        private const val FIELD_GAP_DP = 12
        private const val FIELD_HEIGHT_DP = 48
    }
}
