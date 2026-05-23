package com.shopverse.android.presentation.screen.home

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shopverse.android.core.extension.dp
import com.shopverse.android.core.layout.AppLayout
import com.shopverse.core.model.Product

class ProductAdapter(
    private val onAddToCart: (Product) -> Unit,
    private val onOpenCart: () -> Unit,
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(DIFF) {

    private var cartIds: Set<String> = emptySet()

    fun submit(items: List<Product>, cartIds: Set<String>) {
        val cartChanged = this.cartIds != cartIds
        this.cartIds = cartIds
        submitList(items) {
            if (cartChanged) notifyItemRangeChanged(0, itemCount, PAYLOAD_CART_CHANGED)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val cell = ProductCellView(parent.context).apply {
            layoutParams = AppLayout.Recycler.get(AppLayout.MATCH, AppLayout.WRAP).apply {
                bottomMargin = 12.dp
            }
        }
        return ProductViewHolder(cell, onAddToCart, onOpenCart)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position), isInCart = cartIds.contains(getItem(position).id))
    }

    override fun onBindViewHolder(
        holder: ProductViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.contains(PAYLOAD_CART_CHANGED)) {
            holder.bind(getItem(position), isInCart = cartIds.contains(getItem(position).id))
            return
        }
        super.onBindViewHolder(holder, position, payloads)
    }

    class ProductViewHolder(
        private val cell: ProductCellView,
        private val onAddToCart: (Product) -> Unit,
        private val onOpenCart: () -> Unit,
    ) : RecyclerView.ViewHolder(cell) {
        fun bind(product: Product, isInCart: Boolean) {
            cell.onAddClick = { onAddToCart(product) }
            cell.onCartClick = { onOpenCart() }
            cell.bind(product, isInCart = isInCart)
        }
    }

    companion object {
        private const val PAYLOAD_CART_CHANGED = "cart-changed"

        private val DIFF = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(old: Product, new: Product) = old.id == new.id
            override fun areContentsTheSame(old: Product, new: Product) = old == new
        }
    }
}
