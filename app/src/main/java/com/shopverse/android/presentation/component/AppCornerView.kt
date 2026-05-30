package com.shopverse.android.presentation.component

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.widget.FrameLayout

/**
 * A FrameLayout with customizable round corners
 *
 * @property mRadius general radius of four corners
 * @property mRadiiTL Top Left radius
 * @property mRadiiTR Top Right radius
 * @property mRadiiBR Bottom Right radius
 * @property mRadiiBL Bottom Left radius
 */
@SuppressLint("ViewConstructor")
open class AppCornerView : FrameLayout {

    private var mRadius: Float? = null
    private var mRadiiTL: Float? = null
    private var mRadiiTR: Float? = null
    private var mRadiiBR: Float? = null
    private var mRadiiBL: Float? = null
    /**
     * used when you want single radius on all four corners
     *
     * @param radius desired cornerRadius of all four corners
     * @constructor creates a frameLayout with cornerRadius of [radius]
     */
    constructor(context: Context, radius: Float) : super(context) {
        this.mRadius = radius
    }
    /**
     * used when you want customized corners with different values
     *
     * @param mRadiiTL Top Left radius
     * @param mRadiiTR Top Right radius
     * @param mRadiiBR Bottom Right radius
     * @param mRadiiBL Bottom Left radius
     * @constructor creates a frameLayout with corner radius of given values
     */
    constructor(
        context: Context,
        mRadiiTL: Float,
        mRadiiTR: Float,
        mRadiiBR: Float,
        mRadiiBL: Float
    ) : super(context) {
        this.mRadiiTL = mRadiiTL
        this.mRadiiTR = mRadiiTR
        this.mRadiiBR = mRadiiBR
        this.mRadiiBL = mRadiiBL
    }

    private val path: Path = Path()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val rect = RectF()
        path.reset()
        rect[0f, 0f, w.toFloat()] = h.toFloat()
        if (mRadius != null) {
            path.addRoundRect(rect, mRadius!!, mRadius!!, Path.Direction.CW)
        } else {
            val radii = floatArrayOf(
                mRadiiTL!!, mRadiiTR!!,
                mRadiiTR!!, mRadiiTR!!,
                mRadiiBR!!, mRadiiBR!!,
                mRadiiBL!!, mRadiiBL!!
            )
            path.addRoundRect(rect, radii, Path.Direction.CW)
        }
        path.close()
    }

    override fun dispatchDraw(canvas: Canvas) {
        val save: Int = canvas.save()
        canvas.clipPath(path)
        super.dispatchDraw(canvas)
        canvas.restoreToCount(save)
    }
}