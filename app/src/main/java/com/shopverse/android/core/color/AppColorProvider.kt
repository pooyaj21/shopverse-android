package com.shopverse.android.core.color

import androidx.core.graphics.toColorInt

data object AppColorProvider {
    val black = AppColor("#000000".toColorInt())
    val alwaysBlack = AppColor("#000000".toColorInt(), "#000000".toColorInt())
    val white = AppColor("#FFFFFF".toColorInt())
    val alwaysWhite = AppColor("#FFFFFF".toColorInt(), "#FFFFFF".toColorInt())

    val gray = AppColor("#808080".toColorInt())

    val background = AppColor("#FDFAE8".toColorInt())

    val primaryMain = AppColor("#5047e5".toColorInt(), "#5047e5".toColorInt())

    val buttonFilled = AppColor("#4F46E5".toColorInt(), "#4F46E5".toColorInt())
    val buttonOutline = AppColor("#324155".toColorInt(), "#324155".toColorInt())

    val cardSurface = AppColor("#FFFFFF".toColorInt(), "#1F1F1F".toColorInt())
    val imagePlaceholder = AppColor("#EFEDE3".toColorInt(), "#2A2A2A".toColorInt())
    val discountBadge = AppColor("#E53935".toColorInt(), "#E53935".toColorInt())
    val priceMuted = AppColor("#9AA0A6".toColorInt(), "#9AA0A6".toColorInt())
    val ratingStar = AppColor("#F5A623".toColorInt(), "#F5A623".toColorInt())
}