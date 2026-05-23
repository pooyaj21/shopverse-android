package com.shopverse.android.presentation.screen.home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shopverse.android.core.extension.dp
import com.shopverse.android.presentation.architecture.BaseView
import com.shopverse.core.model.Product

@SuppressLint("ViewConstructor")
class HomeView(
    context: Context,
    val onLoadMore: () -> Unit,
    onAddToCart: (Product) -> Unit,
    onOpenCart: () -> Unit,
    onRetryClickListener: OnClickListener,
) : BaseView.State<HomeUiModel>(context, onRetryClickListener) {

    private val productAdapter = ProductAdapter(
        onAddToCart = onAddToCart,
        onOpenCart = onOpenCart,
    )
    private val layoutManager = GridLayoutManager(context, GRID_SPAN_COUNT)

    private var hasMore: Boolean = false

    override val title: String = "Home"
    override val emptyMessage: String = "No products yet."

    override fun remoteErrorMessage(httpCode: Int, message: String?): String =
        "Couldn't load products ($httpCode). Tap to retry."

    private val recyclerView = RecyclerView(context).apply {
        layoutManager = this@HomeView.layoutManager
        adapter = productAdapter
        clipToPadding = false
        setPadding(GRID_EDGE_DP.dp, GRID_EDGE_DP.dp, GRID_EDGE_DP.dp, GRID_EDGE_DP.dp)
        addItemDecoration(GridSpacingDecoration(GRID_SPAN_COUNT, GRID_GAP_DP.dp))
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                if (dy <= 0 || !hasMore) return
                val total = this@HomeView.layoutManager.itemCount
                val lastVisible = this@HomeView.layoutManager.findLastVisibleItemPosition()
                if (lastVisible >= total - PREFETCH_THRESHOLD) onLoadMore()
            }
        })
    }

    init {
        setContent(recyclerView)
    }

    override fun renderSuccess(model: HomeUiModel) {
        hasMore = model.hasMore
        productAdapter.submit(items = model.items, cartIds = model.cartIds)
    }

    override fun onContentReset() {
        hasMore = false
    }

    private class GridSpacingDecoration(
        private val spanCount: Int,
        private val gap: Int,
    ) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State,
        ) {
            val position = parent.getChildAdapterPosition(view)
            if (position == RecyclerView.NO_POSITION) return
            val column = position % spanCount
            outRect.left = column * gap / spanCount
            outRect.right = gap - (column + 1) * gap / spanCount
            if (position >= spanCount) outRect.top = gap
        }
    }

    companion object {
        private const val PREFETCH_THRESHOLD = 4
        private const val GRID_SPAN_COUNT = 2
        private const val GRID_GAP_DP = 12
        private const val GRID_EDGE_DP = 12
    }
}
