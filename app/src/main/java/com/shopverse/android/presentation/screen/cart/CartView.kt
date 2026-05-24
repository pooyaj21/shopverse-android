package com.shopverse.android.presentation.screen.cart

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shopverse.android.core.extension.dp
import com.shopverse.android.core.layout.AppLayout
import com.shopverse.android.core.layout.margin
import com.shopverse.android.core.recycler.RecyclerViewDecorations
import com.shopverse.android.core.ui.AppVerticalLinearLayout
import com.shopverse.android.presentation.architecture.BaseView
import com.shopverse.android.presentation.component.AppButtonView
import com.shopverse.core.model.LocalCartItem

@SuppressLint("ViewConstructor")
class CartView(
    context: Context,
    onRemoveClickListener: (LocalCartItem) -> Unit,
    onPlaceOrderClickListener: OnClickListener,
) : BaseView.State<CartUiModel>(context, onRetryClickListener = {}) {

    private val cartAdapter = CartAdapter(onRemoveClickListener = onRemoveClickListener)

    override val title: String = "Cart"
    override val emptyMessage: String = "Your cart is empty."

    private val recyclerView = RecyclerView(context).apply {
        layoutManager = LinearLayoutManager(context)
        addItemDecoration(
            RecyclerViewDecorations.VerticalSpaceItemDecoration(margin = ITEM_SPACING_DP.dp)
        )
        setPaddingRelative(EDGE_DP.dp, EDGE_DP.dp, EDGE_DP.dp, EDGE_DP.dp)
        clipToPadding = false
        adapter = cartAdapter
    }

    private val placeOrderButton = AppButtonView(context).apply {
        text = "Place Order"
        setOnClickListener { onPlaceOrderClickListener.onClick(it) }
    }

    private val contentContainer = AppVerticalLinearLayout(context).apply {
        addView(recyclerView, AppLayout.Linear.availableHeightParams())
        addView(
            placeOrderButton,
            AppLayout.Linear.defaultParams().margin(
                start = EDGE_DP.dp,
                end = EDGE_DP.dp,
                top = EDGE_DP.dp,
                bottom = BOTTOM_MARGIN_DP.dp,
            )
        )
    }

    init {
        setContent(contentContainer)
    }

    override fun renderSuccess(model: CartUiModel) {
        cartAdapter.submitList(model.items)
    }

    companion object {
        private const val EDGE_DP = 12
        private const val ITEM_SPACING_DP = 12
        private const val BOTTOM_MARGIN_DP = 70
    }
}
