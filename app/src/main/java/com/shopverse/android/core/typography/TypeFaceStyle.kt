package com.shopverse.android.core.typography

import android.graphics.Typeface

sealed class TypeFaceStyle {
    data object Light : TypeFaceStyle()
    data object Regular : TypeFaceStyle()
    data object Medium : TypeFaceStyle()
    data object SmiBold : TypeFaceStyle()
    data object Bold : TypeFaceStyle()
    data object ExtraBold : TypeFaceStyle()

    inline val typeface: Typeface
        get() {
            return when (this) {
                Light -> TypefaceProvider.light
                Regular -> TypefaceProvider.regular
                Medium -> TypefaceProvider.medium
                SmiBold -> TypefaceProvider.semiBold
                Bold -> TypefaceProvider.bold
                ExtraBold -> TypefaceProvider.extraBold
            }
        }
}