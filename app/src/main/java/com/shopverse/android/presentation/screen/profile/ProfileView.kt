package com.shopverse.android.presentation.screen.profile

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.ScrollView
import com.shopverse.android.core.color.AppColorProvider
import com.shopverse.android.core.extension.dp
import com.shopverse.android.core.extension.setBackgroundColor
import com.shopverse.android.core.layout.AppLayout
import com.shopverse.android.core.layout.margin
import com.shopverse.android.core.ui.AppVerticalLinearLayout
import com.shopverse.android.presentation.architecture.BaseView
import com.shopverse.android.presentation.screen.profile.core.ProfileUiModel
import com.shopverse.android.presentation.screen.profile.view.ProfileItemView

@SuppressLint("ViewConstructor")
class ProfileView(
    context: Context,
    private val onNavigatableClickListener: (ProfileUiModel.Item.Navigatable) -> Unit,
    private val onSimpleClickListener: (ProfileUiModel.Item.Simple) -> Unit,
    private val onEditableClickListener: (ProfileUiModel.Item.Editable) -> Unit,
    private val onTogglableChangeListener: (ProfileUiModel.Item.Togglable) -> Unit,
) : BaseView.State<ProfileUiModel>(context, onRetryClickListener = {}) {

    override val title: String = "Profile"

    private val contentLayout = AppVerticalLinearLayout(context)
    private val scrollView = ScrollView(context).apply {
        addView(contentLayout, AppLayout.Scroll.defaultParams())
    }

    init {
        setContent(scrollView)
    }

    override fun renderSuccess(model: ProfileUiModel) {
        contentLayout.removeAllViews()
        model.items.forEach { item ->
            val view: View = when (item) {
                is ProfileUiModel.Item.Title -> ProfileItemView.Title(context).apply { bind(item) }
                is ProfileUiModel.Item.Navigatable -> ProfileItemView.Navigatable(context).apply {
                    bind(item)
                    setOnClickListener { onNavigatableClickListener(item) }
                }
                is ProfileUiModel.Item.Simple -> ProfileItemView.Simple(context).apply {
                    bind(item)
                    setOnClickListener { onSimpleClickListener(item) }
                }
                is ProfileUiModel.Item.Editable -> ProfileItemView.Editable(context).apply {
                    bind(item)
                    setOnClickListener { onEditableClickListener(item) }
                }
                is ProfileUiModel.Item.Info -> ProfileItemView.Info(context).apply { bind(item) }
                is ProfileUiModel.Item.Togglable -> ProfileItemView.Togglable(context).apply {
                    bind(item)
                    setOnCheckedChangeListener { _, isChecked ->
                        onTogglableChangeListener(
                            when (item) {
                                is ProfileUiModel.Item.Togglable.DarkMode ->
                                    ProfileUiModel.Item.Togglable.DarkMode(isChecked)
                            }
                        )
                    }
                }
                is ProfileUiModel.Item.Separator -> View(context).apply {
                    setBackgroundColor(AppColorProvider.imagePlaceholder)
                }
            }
            val params = if (item is ProfileUiModel.Item.Separator) {
                AppLayout.Linear.horizontalLine().margin(
                    start = if (item.isLast) 0 else SEPARATOR_INDENT_DP.dp,
                )
            } else {
                AppLayout.Linear.defaultParams()
            }
            contentLayout.addView(view, params)
        }
    }

    companion object {
        private const val SEPARATOR_INDENT_DP = 16
    }
}
