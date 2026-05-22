package com.shopverse.android.core.color

import android.content.Context
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import com.shopverse.android.core.extension.isDarkModeOn

class AppColor(
    @ColorInt private val light: Int,
    @ColorInt private val dark: Int? = null
) {
    val computedLight: Int
        get() {
            return light
        }
    val computedDark: Int
        get() {
            return dark ?: inverted(light)
        }

    fun sameColorAlpha(@IntRange(from = 0, to = 255) alpha: Int): AppColor {
        return AppColor(
            light = changeAlpha(computedLight, alpha),
            dark = changeAlpha(computedDark, alpha)
        )
    }

    fun value(context: Context): Int {
        return when (context.isDarkModeOn) {
            true -> computedDark
            false -> computedLight
        }
    }

    private data class ARGB(val alpha: Int, val red: Int, val green: Int, val blue: Int) {
        constructor(@ColorInt color: Int) : this(
            Color.alpha(color),
            Color.red(color),
            Color.green(color),
            Color.blue(color)
        )
    }

    @ColorInt
    private fun inverted(@ColorInt color: Int): Int {
        val argb = ARGB(color)
        return Color.argb(
            argb.alpha,
            255 - argb.red,
            255 - argb.green,
            255 - argb.blue
        )
    }

    @ColorInt
    private fun changeAlpha(@ColorInt color: Int, @IntRange(from = 0, to = 255) alpha: Int): Int {
        val argb = ARGB(color)
        return Color.argb(alpha, argb.red, argb.green, argb.blue)
    }

    fun alpha(@FloatRange(from = 0.0, to = 1.0) alpha: Float): AppColor {
        return this.sameColorAlpha((255 * alpha).toInt())
    }
}