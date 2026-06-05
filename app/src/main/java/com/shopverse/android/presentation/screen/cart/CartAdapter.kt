package com.shopverse.android.presentation.screen.cart

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shopverse.android.core.layout.AppLayout
import com.shopverse.core.model.LocalCartItem

class CartAdapter(
    private val onItemClickListener: (LocalCartItem) -> Unit,
    private val onRemoveClickListener: (LocalCartItem) -> Unit,
) : ListAdapter<LocalCartItem, CartAdapter.CartViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val cell = CartCellView(
            context = parent.context,
            onItemClickListener = onItemClickListener,
            onRemoveClickListener = onRemoveClickListener,
        ).apply {
            layoutParams = AppLayout.Recycler.defaultParams()
        }
        return CartViewHolder(cell)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CartViewHolder(
        private val cell: CartCellView,
    ) : RecyclerView.ViewHolder(cell) {
        fun bind(item: LocalCartItem) {
            cell.bind(item)
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<LocalCartItem>() {
            override fun areItemsTheSame(old: LocalCartItem, new: LocalCartItem) = old.id == new.id
            override fun areContentsTheSame(old: LocalCartItem, new: LocalCartItem) = old == new
        }
    }
}
