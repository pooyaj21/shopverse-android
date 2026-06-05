package com.shopverse.android.presentation.screen.orders

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shopverse.android.core.extension.dp
import com.shopverse.android.core.recycler.RecyclerViewDecorations
import com.shopverse.android.presentation.architecture.BaseView

@SuppressLint("ViewConstructor")
class OrdersView(
    context: Context,
    onOrderClick: (String) -> Unit,
    private val onLoadMore: () -> Unit,
    onRetryClickListener: () -> Unit,
) : BaseView.State<OrdersUiModel>(context, onRetryClickListener) {

    private val ordersAdapter = OrdersAdapter(onOrderClick = onOrderClick)
    private val layoutManager = LinearLayoutManager(context)

    private var hasMore: Boolean = false

    override val title: String = "Orders"
    override val emptyMessage: String = "You haven't placed any orders yet."

    override fun remoteErrorMessage(httpCode: Int, message: String?): String =
        "Couldn't load orders ($httpCode). Tap to retry."

    private val recyclerView = RecyclerView(context).apply {
        layoutManager = this@OrdersView.layoutManager
        addItemDecoration(
            RecyclerViewDecorations.VerticalSpaceItemDecoration(margin = ITEM_SPACING_DP.dp)
        )
        setPaddingRelative(EDGE_DP.dp, EDGE_DP.dp, EDGE_DP.dp, EDGE_DP.dp)
        clipToPadding = false
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                if (dy <= 0 || !hasMore) return
                val total = this@OrdersView.layoutManager.itemCount
                val lastVisible = this@OrdersView.layoutManager.findLastVisibleItemPosition()
                if (lastVisible >= total - PREFETCH_THRESHOLD) onLoadMore()
            }
        })
        adapter = ordersAdapter
    }

    init {
        setContent(recyclerView)
    }

    override fun renderSuccess(model: OrdersUiModel) {
        hasMore = model.hasMore
        ordersAdapter.submitList(model.items)
    }

    override fun onContentReset() {
        hasMore = false
    }

    companion object {
        private const val PREFETCH_THRESHOLD = 4
        private const val EDGE_DP = 12
        private const val ITEM_SPACING_DP = 12
    }
}
