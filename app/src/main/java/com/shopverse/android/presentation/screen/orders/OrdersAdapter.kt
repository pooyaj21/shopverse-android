package com.shopverse.android.presentation.screen.orders

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shopverse.android.core.layout.AppLayout
import com.shopverse.core.model.OrderSummary

class OrdersAdapter(
    private val onOrderClick: (String) -> Unit
) : ListAdapter<OrderSummary, OrdersAdapter.OrderViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val cell = OrderCellView(context = parent.context, onOrderClick = onOrderClick).apply {
            layoutParams = AppLayout.Recycler.defaultParams()
        }
        return OrderViewHolder(cell)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class OrderViewHolder(
        private val cell: OrderCellView,
    ) : RecyclerView.ViewHolder(cell) {
        fun bind(order: OrderSummary) {
            cell.bind(order)
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<OrderSummary>() {
            override fun areItemsTheSame(old: OrderSummary, new: OrderSummary) = old.id == new.id
            override fun areContentsTheSame(old: OrderSummary, new: OrderSummary) = old == new
        }
    }
}
