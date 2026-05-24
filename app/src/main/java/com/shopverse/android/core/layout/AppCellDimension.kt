package com.shopverse.android.core.layout

import com.shopverse.android.core.Screen

class AppCellDimension(
    val widthRatio: Float?,
    val width: Int,
    val height: Int?,
    val spacing: Int
) {

    data class Size(val width: Int, val height: Int)

    data class Dimension(
        val size: Size,
        val cols: Int,
        val spacing: Int // horizontal spacing between columns, and the side margins
    )

    fun dimensions(screenWidth: Int? = null): Dimension {
        val effectiveWidth = screenWidth ?: Screen.size.width
        val colNumber = effectiveWidth / width
        val sumOfCells = effectiveWidth - (spacing * (colNumber + 1))
        val cellWidth = sumOfCells / colNumber
        val cellHeight = if (widthRatio == null) height
            ?: throw IllegalArgumentException("Fill widthRatio or height") else (cellWidth / widthRatio).toInt()
        return Dimension(Size(cellWidth, cellHeight), colNumber, spacing)
    }
}