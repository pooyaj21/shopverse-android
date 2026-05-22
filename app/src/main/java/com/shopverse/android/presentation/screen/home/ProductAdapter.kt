package com.shopverse.android.presentation.screen.home

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shopverse.android.core.color.AppColorProvider
import com.shopverse.android.core.extension.dp
import com.shopverse.android.core.extension.setPadding
import com.shopverse.android.core.extension.setTextColor
import com.shopverse.android.core.extension.setTypography
import com.shopverse.android.core.layout.AppLayout
import com.shopverse.android.core.typography.Typography
import com.shopverse.android.presentation.component.AppTextView
import com.shopverse.core.model.Product

class ProductAdapter : ListAdapter<Product, ProductAdapter.ProductViewHolder>(DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val titleView = AppTextView(parent.context).apply {
            layoutParams = AppLayout.Recycler.defaultParams()
            setPadding(horizontal = 16.dp, vertical = 14.dp)
            setTypography(Typography.M16)
            setTextColor(AppColorProvider.black)
        }
        return ProductViewHolder(titleView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ProductViewHolder(private val titleView: AppTextView) : RecyclerView.ViewHolder(titleView) {
        fun bind(product: Product) {
            titleView.text = product.title
        }
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(old: Product, new: Product) = old.id == new.id
            override fun areContentsTheSame(old: Product, new: Product) = old == new
        }
    }
}
