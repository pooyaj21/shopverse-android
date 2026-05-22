package com.shopverse.android.presentation.splash

import android.animation.Animator
import android.annotation.SuppressLint
import android.content.Context
import android.widget.FrameLayout
import com.airbnb.lottie.LottieAnimationView
import com.shopverse.android.R
import com.shopverse.android.core.color.AppColorProvider
import com.shopverse.android.core.extension.setBackgroundColor
import com.shopverse.android.core.layout.AppLayout

@SuppressLint("ViewConstructor")
class SplashView(
    context: Context,
    private val onSplashEnd: () -> Unit,
) : FrameLayout(context) {

    private var splashEnded = false

    private val lottie = LottieAnimationView(context).apply {
        setAnimation(R.raw.splash)
        playAnimation()
        addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                handleSplashEnded()
            }

            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
    }

    init {
        setBackgroundColor(AppColorProvider.primaryMain)
        addView(lottie, AppLayout.Frame.fullScreen())
    }

    private fun handleSplashEnded() {
        if (splashEnded) return
        splashEnded = true
        onSplashEnd()
    }
}
