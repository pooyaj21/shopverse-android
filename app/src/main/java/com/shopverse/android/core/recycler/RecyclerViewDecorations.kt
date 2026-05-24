package com.shopverse.android.core.recycler

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.shopverse.android.core.extension.isRTL

class RecyclerViewDecorations {

    class HorizontalSpaceItemDecoration(
        context: Context,
        margin: Int = 0
    ) : RecyclerView.ItemDecoration() {

        private val isRtl = context.isRTL()
        private val halfMargin = margin / 2
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val itemPosition = parent.getChildAdapterPosition(view)
            if (itemPosition == RecyclerView.NO_POSITION)
                return

            val isLastItem = itemPosition == parent.adapter?.itemCount?.minus(1)
            val isFirstItem = itemPosition == 0

            outRect.left = if (isRtl) {
                if (isLastItem) 0 else halfMargin
            } else {
                if (isFirstItem) 0 else halfMargin
            }

            outRect.right = if (isRtl) {
                if (isFirstItem) 0 else halfMargin
            } else {
                if (isLastItem) 0 else halfMargin
            }
        }

    }

    class VerticalSpaceItemDecoration(
        private val margin: Int = 0
    ) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val itemPosition = parent.getChildAdapterPosition(view)
            if (itemPosition == RecyclerView.NO_POSITION)
                return

            val isFirstItem = itemPosition == 0
            outRect.top = if (isFirstItem) 0 else margin
        }

    }

    class NoLastItemDividerDecorator(
        val context: Context,
        orientation: Int
    ) : DividerItemDecoration(context, orientation) {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)

            val position = parent.getChildAdapterPosition(view)
            val last = parent.adapter?.itemCount ?: 0

            if (position == last - 1) {
                outRect.set(0, 0, 0, 0)
            }
        }

    }


    class GridSpacingItemDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.top = spacing / 2
            outRect.left = spacing / 2
            outRect.right = spacing / 2
            outRect.bottom = spacing / 2
        }

    }

}