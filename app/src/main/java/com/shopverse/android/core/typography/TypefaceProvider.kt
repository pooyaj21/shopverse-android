package com.shopverse.android.core.typography

import android.content.Context
import android.graphics.Typeface
import androidx.annotation.FontRes
import androidx.core.content.res.ResourcesCompat
import com.shopverse.android.R
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

data object TypefaceProvider : KoinComponent {

    private val context: Context by inject()

    val light: Typeface by lazy { load(R.font.inter_tight_light) }
    val regular: Typeface by lazy { load(R.font.inter_tight_regular) }
    val medium: Typeface by lazy { load(R.font.inter_tight_medium) }
    val semiBold: Typeface by lazy { load(R.font.inter_tight_semi_bold) }
    val bold: Typeface by lazy { load(R.font.inter_tight_bold) }
    val extraBold: Typeface by lazy { load(R.font.inter_tight_extra_bold) }

    private fun load(@FontRes id: Int): Typeface =
        ResourcesCompat.getFont(context, id) ?: Typeface.DEFAULT
}
