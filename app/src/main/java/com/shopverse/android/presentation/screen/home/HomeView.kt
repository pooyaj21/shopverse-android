package com.shopverse.android.presentation.screen.home

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shopverse.android.core.Screen
import com.shopverse.android.core.extension.dp
import com.shopverse.android.core.recycler.RecyclerViewDecorations
import com.shopverse.android.presentation.architecture.BaseView
import com.shopverse.android.presentation.feature.prodcutList.ProductAdapter
import com.shopverse.android.presentation.feature.prodcutList.productCellDimension
import com.shopverse.core.model.Product

@SuppressLint("ViewConstructor")
class HomeView(
    context: Context,
    val onLoadMore: () -> Unit,
    onProductClickListener: (Product) -> Unit,
    onAddToCartClickListener: (Product) -> Unit,
    onCartClickListener: () -> Unit,
    onRetryClickListener: OnClickListener,
) : BaseView.State<HomeUiModel>(context, onRetryClickListener) {

    private val dims = productCellDimension.dimensions(Screen.size.width - (GRID_EDGE_DP.dp * 2))

    private val productAdapter = ProductAdapter(
        dims = dims,
        onProductClickListener = onProductClickListener,
        onAddToCartClickListener = onAddToCartClickListener,
        onCartClickListener = onCartClickListener,
    )
    private val layoutManager = GridLayoutManager(context, dims.cols)

    private var hasMore: Boolean = false

    override val title: String = "Home"
    override val emptyMessage: String = "No products yet."

    override fun remoteErrorMessage(httpCode: Int, message: String?): String =
        "Couldn't load products ($httpCode). Tap to retry."

    private val recyclerView = RecyclerView(context).apply {
        addItemDecoration(
            RecyclerViewDecorations.GridSpacingItemDecoration(
                spacing = dims.spacing,
            )
        )
        setPaddingRelative(GRID_EDGE_DP.dp, 0, GRID_EDGE_DP.dp, 50.dp)
        clipToPadding = false
        layoutManager = this@HomeView.layoutManager
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                if (dy <= 0 || !hasMore) return
                val total = this@HomeView.layoutManager.itemCount
                val lastVisible = this@HomeView.layoutManager.findLastVisibleItemPosition()
                if (lastVisible >= total - PREFETCH_THRESHOLD) onLoadMore()
            }
        })
        adapter = productAdapter
    }

    init {
        setContent(recyclerView)
    }

    override fun renderSuccess(model: HomeUiModel) {
        hasMore = model.hasMore
        productAdapter.submitList(model.items)
    }

    override fun onContentReset() {
        hasMore = false
    }

    companion object {
        private const val PREFETCH_THRESHOLD = 4
        private const val GRID_EDGE_DP = 12
    }
}
