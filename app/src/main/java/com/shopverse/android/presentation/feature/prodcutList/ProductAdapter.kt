package com.shopverse.android.presentation.feature.prodcutList

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shopverse.android.core.extension.dp
import com.shopverse.android.core.layout.AppLayout
import com.shopverse.core.model.Product

class ProductAdapter(
    private val onAddToCartClickListener: (Product) -> Unit,
    private val onCartClickListener: () -> Unit
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val cell = ProductCellView(
            context = parent.context,
            onAddToCartClickListener = {
                onAddToCartClickListener(it)
                notifyDataSetChanged()
            },
            onCartClickListener = onCartClickListener
        ).apply {
            layoutParams = AppLayout.Recycler.get(AppLayout.MATCH, AppLayout.WRAP).apply {
                bottomMargin = 12.dp
            }
        }
        return ProductViewHolder(cell)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ProductViewHolder(
        val cell: ProductCellView,
    ) : RecyclerView.ViewHolder(cell) {
        fun bind(product: Product) {
            cell.bind(product)
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(old: Product, new: Product) = old.id == new.id
            override fun areContentsTheSame(old: Product, new: Product) = old == new
        }
    }
}