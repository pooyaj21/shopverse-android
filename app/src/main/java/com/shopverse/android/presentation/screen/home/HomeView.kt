package com.shopverse.android.presentation.screen.home

import android.annotation.SuppressLint
import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.shopverse.android.presentation.architecture.BaseView

@SuppressLint("ViewConstructor")
class HomeView(
    context: Context,
    val onLoadMore: () -> Unit,
    onRetryClickListener: OnClickListener
) : BaseView.State<HomeUiModel>(context, onRetryClickListener) {

    private val productAdapter = ProductAdapter()
    private val layoutManager = LinearLayoutManager(context)

    private var hasMore: Boolean = false

    override val title: String = "Home"
    override val emptyMessage: String = "No products yet."

    override fun remoteErrorMessage(httpCode: Int, message: String?): String =
        "Couldn't load products ($httpCode). Tap to retry."

    private val recyclerView = RecyclerView(context).apply {
        layoutManager = this@HomeView.layoutManager
        adapter = productAdapter
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
        productAdapter.submitList(model.items)
    }

    override fun onContentReset() {
        hasMore = false
    }

    companion object {
        private const val PREFETCH_THRESHOLD = 4
    }
}
