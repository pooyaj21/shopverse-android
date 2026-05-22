package com.shopverse.android.core.layout

import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.updateMarginsRelative
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.AppBarLayout
import com.shopverse.android.core.extension.dp

class AppLayout {

    companion object {
        const val MATCH = ViewGroup.LayoutParams.MATCH_PARENT
        const val WRAP = ViewGroup.LayoutParams.WRAP_CONTENT
    }

    class Linear(width: Int, height: Int, weight: Float = 0F) :
        LinearLayout.LayoutParams(width, height, weight) {
        fun gravity(gravity: Int): Linear {
            this.gravity = gravity
            return this
        }

        companion object {
            fun defaultParams() = Linear(MATCH, WRAP)
            fun wrapContent() = Linear(WRAP, WRAP)
            fun fullScreen() = Linear(MATCH, MATCH)
            fun availableHeightParams(weight: Float = 1F) = Linear(MATCH, 0, weight)
            fun availableWidthParams(weight: Float = 1F) = Linear(0, WRAP, weight)
            fun justOccupyWidth() = Linear(0, 1, 1F)
            fun justOccupyHeight() = Linear(1, 0, 1F)
            fun justOccupyScreen() = Linear(MATCH, MATCH, 1F)
            fun get(w: Int, h: Int) = Linear(w, h)
            fun get(w: Int) = Linear(w, w)
            fun horizontalLine() = get(MATCH, 1.dp)
            fun verticalLine() = get(1.dp, MATCH)
        }
    }

    class Frame(width: Int, height: Int) : FrameLayout.LayoutParams(width, height) {
        fun gravity(gravity: Int): Frame {
            this.gravity = gravity
            return this
        }

        companion object {
            fun defaultParams() = Frame(MATCH, WRAP)
            fun wrapContent() = Frame(WRAP, WRAP)
            fun fullScreen() = Frame(MATCH, MATCH)
            fun get(w: Int, h: Int) = Frame(w, h)
            fun get(w: Int) = Frame(w, w)
            fun horizontalLine() = get(MATCH, 1.dp)
            fun verticalLine() = get(1.dp, MATCH)
        }
    }

    class Relative(width: Int, height: Int) : RelativeLayout.LayoutParams(width, height) {
        fun above(viewId: Int) = apply { addRule(RelativeLayout.ABOVE, viewId) }
        fun below(viewId: Int) = apply { addRule(RelativeLayout.BELOW, viewId) }
        fun toStartOf(viewId: Int) = apply { addRule(RelativeLayout.START_OF, viewId) }
        fun toEndOf(viewId: Int) = apply { addRule(RelativeLayout.END_OF, viewId) }
        fun alignParentBottom() = apply { addRule(RelativeLayout.ALIGN_PARENT_BOTTOM) }
        fun alignParentTop() = apply { addRule(RelativeLayout.ALIGN_PARENT_TOP) }
        fun alignParentStart() = apply { addRule(RelativeLayout.ALIGN_PARENT_START) }
        fun alignParentEnd() = apply { addRule(RelativeLayout.ALIGN_PARENT_END) }
        fun centerInParent() = apply { addRule(RelativeLayout.CENTER_IN_PARENT) }
        fun centerVertical() = apply { addRule(RelativeLayout.CENTER_VERTICAL) }
        fun centerHorizontal() = apply { addRule(RelativeLayout.CENTER_HORIZONTAL) }

        companion object {
            fun defaultParams() = Relative(MATCH, WRAP)
            fun wrapContent() = Relative(WRAP, WRAP)
            fun fullScreen() = Relative(MATCH, MATCH)
            fun get(w: Int, h: Int) = Relative(w, h)
            fun get(w: Int) = Relative(w, w)
            fun horizontalLine() = Linear.get(MATCH, 1.dp)
            fun verticalLine() = Linear.get(1.dp, MATCH)
        }
    }

    class Constraint(width: Int, height: Int) : ConstraintLayout.LayoutParams(width, height) {

        companion object {
            fun defaultParams() = Constraint(0, WRAP)
            fun wrapContent() = Constraint(WRAP, WRAP)
            fun fullScreen() = Constraint(0, 0)
            fun get(w: Int, h: Int) = Constraint(w, h)
            fun get(w: Int) = Constraint(w, w)
            fun horizontalLine() = Constraint(0, 1.dp)
            fun verticalLine() = Constraint(1.dp, 0)
        }
    }

    class ConstraintGuideLine(width: Int, height: Int) :
        ConstraintLayout.LayoutParams(width, height) {

        companion object {
            fun defaultParams() = ConstraintGuideLine(0, WRAP)
            fun wrapContent() = ConstraintGuideLine(WRAP, WRAP)
            fun fullScreen() = ConstraintGuideLine(0, 0)
            fun get(w: Int, h: Int) = ConstraintGuideLine(w, h)
            fun get(w: Int) = ConstraintGuideLine(w, w)
        }

        fun verticalGuideLine() = apply { orientation = VERTICAL }
        fun horizontalGuideLine() = apply { orientation = HORIZONTAL }
        fun guideLinePercent(percent: Float) = apply { guidePercent = percent }
    }

    class Scroll(width: Int, height: Int) : FrameLayout.LayoutParams(width, height) {

        companion object {
            fun defaultParams() = Scroll(MATCH, WRAP)
            fun wrapContent() = Scroll(WRAP, WRAP)
            fun fullScreen() = Scroll(MATCH, MATCH)
            fun get(w: Int, h: Int) = Scroll(w, h)
            fun get(w: Int) = Scroll(w, w)
        }
    }

    class Card(width: Int, height: Int) : FrameLayout.LayoutParams(width, height) {

        companion object {
            fun defaultParams() = Card(MATCH, WRAP)
            fun wrapContent() = Card(WRAP, WRAP)
            fun fullScreen() = Card(MATCH, MATCH)
            fun get(w: Int, h: Int) = Card(w, h)
            fun get(w: Int) = Card(w, w)
        }
    }

    class Drawer(width: Int, height: Int) : DrawerLayout.LayoutParams(width, height) {
        fun gravity(gravity: Int): Drawer {
            this.gravity = gravity
            return this
        }

        companion object {
            fun fullScreen() = Drawer(MATCH, MATCH)
            fun get(w: Int, h: Int) = Drawer(w, h)
            fun get(w: Int) = Drawer(w, w)
        }
    }

    class Coordinator(width: Int, height: Int) : CoordinatorLayout.LayoutParams(width, height) {

        companion object {
            fun defaultParams() = Coordinator(MATCH, WRAP)

            fun scroller(): Coordinator {
                val params = Coordinator(MATCH, MATCH)
                params.behavior = AppBarLayout.ScrollingViewBehavior()
                return params
            }

            fun get(w: Int, h: Int) = Coordinator(w, h)
            fun get(w: Int) = Coordinator(w, w)
        }
    }

    class AppBar(width: Int, height: Int) : AppBarLayout.LayoutParams(width, height) {

        companion object {
            fun defaultParams() = AppBar(MATCH, WRAP)
            fun get(w: Int, h: Int) = AppBar(w, h)
            fun get(w: Int) = AppBar(w, w)

            fun scrollFlags(flags: Int): AppBar {
                val params = defaultParams()
                params.scrollFlags = flags
                return params
            }

            fun stay(): AppBar = scrollFlags(SCROLL_FLAG_ENTER_ALWAYS)
            fun scroll(): AppBar = scrollFlags(SCROLL_FLAG_SCROLL)
        }
    }

    class NestedScroll(width: Int, height: Int) : FrameLayout.LayoutParams(width, height) {
        companion object {
            fun fullScreen() = NestedScroll(MATCH, MATCH)
            fun wrapContent() = NestedScroll(WRAP, WRAP)
            fun defaultParams() = NestedScroll(MATCH, WRAP)
            fun get(w: Int, h: Int) = NestedScroll(w, h)
            fun get(w: Int) = NestedScroll(w, w)
        }
    }

    class Recycler(width: Int, height: Int) : RecyclerView.LayoutParams(width, height) {
        companion object {
            fun defaultParams() = Recycler(MATCH, WRAP)
            fun wrapContent() = Recycler(WRAP, WRAP)
            fun fullSize() = Recycler(MATCH, MATCH)
            fun get(w: Int, h: Int) = Recycler(w, h)
            fun get(w: Int) = Recycler(w, w)
        }
    }

    class Group(width: Int, height: Int) : ViewGroup.LayoutParams(width, height) {

        companion object {
            fun defaultParams() = Group(MATCH, WRAP)
            fun wrapContent() = Group(WRAP, WRAP)
            fun fullScreen() = Group(MATCH, MATCH)
            fun get(w: Int, h: Int) = Group(w, h)
            fun get(w: Int) = Group(w, w)
        }
    }

}

fun ViewGroup.MarginLayoutParams.margin(value: Int) = apply {
    updateMarginsRelative(value, value, value, value)
}

fun ViewGroup.MarginLayoutParams.margin(
    start: Int = marginStart,
    top: Int = topMargin,
    end: Int = marginEnd,
    bottom: Int = bottomMargin
) = apply {
    updateMarginsRelative(start, top, end, bottom)
}

fun ViewGroup.MarginLayoutParams.margin(
    horizontal: Int? = null,
    vertical: Int? = null,
) = apply {
    updateMarginsRelative(
        start = horizontal ?: marginStart,
        top = vertical ?: topMargin,
        end = horizontal ?: marginEnd,
        bottom = vertical ?: bottomMargin
    )
}
