package com.shopverse.android.core.animation

import android.animation.ArgbEvaluator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import androidx.core.animation.doOnEnd
import com.shopverse.android.core.color.AppColor

object AnimationUtils {

    fun createColorAnimator(
        fromColor: Int,
        toColor: Int,
        duration: Long? = null,
        interpolator: TimeInterpolator? = null,
        animateColor: (Int) -> Unit
    ): ValueAnimator = ValueAnimator.ofObject(ArgbEvaluator(), fromColor, toColor)
        .apply {
            if (duration != null)
                this.duration = duration
            if (interpolator != null)
                this.interpolator = interpolator
            addUpdateListener { animator ->
                animateColor.invoke(animator.animatedValue as Int)
            }
            doOnEnd {
                animateColor.invoke(toColor)
            }
        }

    fun createColorAnimator(
        context: Context,
        fromColor: AppColor,
        toColor: AppColor,
        duration: Long? = null,
        interpolator: TimeInterpolator? = null,
        animateColor: (AppColor) -> Unit
    ): ValueAnimator = createColorAnimator(
        fromColor = fromColor.value(context),
        toColor = toColor.value(context),
        duration = duration,
        interpolator = interpolator,
        animateColor = {
            animateColor.invoke(AppColor(it, it))
        })


}