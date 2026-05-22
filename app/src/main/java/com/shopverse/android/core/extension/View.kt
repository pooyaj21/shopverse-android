package com.shopverse.android.core.extension

import android.view.View

fun View.setPadding(
    left: Int = paddingLeft,
    top: Int = paddingTop,
    right: Int = paddingRight,
    bottom: Int = paddingBottom
) {
    this.setPadding(left, top, right, bottom)
}

fun View.setPadding(
    horizontal: Int? = null,
    vertical: Int? = null,
) {
    this.setPadding(
        left = horizontal ?: paddingLeft,
        top = vertical ?: paddingTop,
        right = horizontal ?: paddingRight,
        bottom = vertical ?: paddingBottom
    )
}