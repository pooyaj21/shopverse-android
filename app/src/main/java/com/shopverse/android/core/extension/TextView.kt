package com.shopverse.android.core.extension

import android.text.TextUtils
import android.util.TypedValue
import android.widget.TextView
import com.shopverse.android.core.color.AppColor
import com.shopverse.android.core.typography.TypeFaceStyle
import com.shopverse.android.core.typography.Typography

fun TextView.setTypeface(style: TypeFaceStyle) {
    typeface = style.typeface
}

fun TextView.setTextSizeInPixel(px: Int) {
    setTextSize(TypedValue.COMPLEX_UNIT_PX, px.toFloat())
}

fun TextView.applySingleLine() {
    setSingleLine()
    setLines(1)
    maxLines = 1
    ellipsize = TextUtils.TruncateAt.END
}

fun TextView.applyMultiLine(lines: Int) {
    setLines(lines)
    maxLines = lines
    ellipsize = TextUtils.TruncateAt.END
    setHorizontallyScrolling(false)
}

fun TextView.applyMaxLine(lines: Int) {
    maxLines = lines
    ellipsize = TextUtils.TruncateAt.END
}


fun TextView.setTypography(typography: Typography) {
    setTypeface(typography.typeFaceStyle)
    setTextSizeInPixel(typography.fontSize.dp)
}

fun TextView.setTextColor(color: AppColor) {
    setTextColor(color.value(context))
}