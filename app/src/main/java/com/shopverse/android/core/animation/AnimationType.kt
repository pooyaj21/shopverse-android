package com.shopverse.android.core.animation

import android.animation.*
import android.annotation.SuppressLint
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.IntRange
import androidx.core.animation.doOnStart
import com.shopverse.android.core.color.AppColor

sealed class AnimationType {
    abstract fun createAnimator(view: View): Animator
    abstract fun reverse(): AnimationType

    data class Alpha(val target: Float, val start: Float = 1F) : AnimationType() {
        override fun createAnimator(view: View): Animator =
            ObjectAnimator.ofFloat(view, "alpha", start, target)

        override fun reverse(): AnimationType = this.copy(start = target, target = start)
    }

    data class BackgroundAlpha(
        @IntRange(from = 0, to = 255) val target: Int,
        @IntRange(from = 0, to = 255) val start: Int
    ) : AnimationType() {
        override fun createAnimator(view: View): Animator {
            return ValueAnimator.ofInt(start, target).apply {
                addUpdateListener { valueAnimator ->
                    view.background.alpha = valueAnimator.animatedValue as Int
                }
            }
        }

        override fun reverse(): AnimationType = this.copy(start = target, target = start)
    }

    data class Scale(val target: Float, val start: Float = 1F) : AnimationType() {
        override fun createAnimator(view: View): Animator = ObjectAnimator.ofPropertyValuesHolder(
            view,
            PropertyValuesHolder.ofFloat("scaleX", start, target),
            PropertyValuesHolder.ofFloat("scaleY", start, target)
        )

        override fun reverse(): AnimationType = this.copy(start = target, target = start)
    }

    abstract class ArgbAnimator(
        protected val fromColor: AppColor,
        protected val toColor: AppColor
    ) : AnimationType() {
        protected abstract fun setAnimatedValue(view: View, color: Int)

        override fun createAnimator(view: View): Animator {
            return ValueAnimator.ofObject(
                ArgbEvaluator(),
                fromColor.value(view.context),
                toColor.value(view.context)
            ).apply {
                addUpdateListener { valueAnimator ->
                    setAnimatedValue(view, valueAnimator.animatedValue as Int)
                }
            }
        }
    }

    class BackgroundColor(fromColor: AppColor, toColor: AppColor) : ArgbAnimator(fromColor, toColor) {
        override fun setAnimatedValue(view: View, color: Int) {
            val background = view.background
            if (background is GradientDrawable) {
                background.setColor(color)
            } else {
                view.setBackgroundColor(color)
            }
        }

        override fun reverse(): AnimationType =
            BackgroundColor(fromColor = toColor, toColor = fromColor)
    }

    class StrokeColor(fromColor: AppColor, toColor: AppColor) : ArgbAnimator(fromColor, toColor) {
        private var strokeWidth = -1
        override fun createAnimator(view: View): Animator {
            val background = view.background
            if (background is GradientDrawable) {
                try {
                    @SuppressLint("DiscouragedPrivateApi")
                    val field = GradientDrawable::class.java.getDeclaredField("mStrokePaint")
                    field.isAccessible = true
                    val strokePaint = field.get(background) as Paint
                    strokeWidth = strokePaint.strokeWidth.toInt()
                } catch (e: NoSuchFieldException) {
                    // Nothing
                }
            }
            return super.createAnimator(view)
        }

        override fun setAnimatedValue(view: View, color: Int) {
            (view.background as? GradientDrawable)?.setStroke(strokeWidth, color)
        }

        override fun reverse(): AnimationType =
            StrokeColor(fromColor = toColor, toColor = fromColor)
    }

    class TextColor(fromColor: AppColor, toColor: AppColor) : ArgbAnimator(fromColor, toColor) {
        override fun setAnimatedValue(view: View, color: Int) {
            (view as? TextView)?.setTextColor(color)
        }

        override fun reverse(): AnimationType = TextColor(fromColor = toColor, toColor = fromColor)
    }

    class BackgroundTintColor(fromColor: AppColor, toColor: AppColor) :
        ArgbAnimator(fromColor, toColor) {
        override fun setAnimatedValue(view: View, color: Int) {
            view.background.setTint(color)
        }

        override fun reverse(): AnimationType =
            BackgroundTintColor(fromColor = toColor, toColor = fromColor)
    }

}

fun View.addAnimate(vararg animationTypes: AnimationType, duration: Long = 300L) {
    var isFirstTime = true
    val pressedAnimatorSet = AnimatorSet().apply {
        playTogether(
            *animationTypes.map {
                it.createAnimator(this@addAnimate).setDuration(duration)
            }.toTypedArray()
        )
        doOnStart {
            if (isFirstTime) {
                isFirstTime = false
                stateListAnimator.addState(intArrayOf(), AnimatorSet().apply {
                    playTogether(*animationTypes.map {
                        it.reverse().createAnimator(this@addAnimate).setDuration(duration)
                    }.toTypedArray())
                })
            }
        }
    }

    this.stateListAnimator = StateListAnimator().apply {
        addState(intArrayOf(android.R.attr.state_pressed), pressedAnimatorSet)
    }
}

 fun showViewWithTransition(fixedView: View,hideAndShowView: View) {
    if (hideAndShowView.visibility != View.VISIBLE) {
        hideAndShowView.visibility = View.VISIBLE

        // Animate width change using TransitionManager
        val layoutParams = fixedView.layoutParams
        layoutParams.width = layoutParams.width - hideAndShowView.width

        TransitionManager.beginDelayedTransition(fixedView.parent as ViewGroup)
        fixedView.layoutParams = layoutParams
    }
}

 fun hideViewWithTransition(fixedView: View,hideAndShowView: View) {
    if (hideAndShowView.visibility != View.GONE) {
        // Expand searchView to its full width
        val layoutParams = fixedView.layoutParams
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT

       TransitionManager.beginDelayedTransition(fixedView.parent as ViewGroup)
        fixedView.layoutParams = layoutParams

        hideAndShowView.visibility = View.GONE // Hide after animation
    }
}