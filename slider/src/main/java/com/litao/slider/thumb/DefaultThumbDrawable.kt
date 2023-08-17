package com.litao.slider.thumb

import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import kotlin.math.min


/**
 * @author : litao
 * @date   : 2023/8/14 11:43
 */
class DefaultThumbDrawable : Drawable(), IBaseThumbDrawable {

    private val thumbPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)

    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)

    private val shadowPaint = Paint()

    /**
     * thumb bounds rect
     */
    private val rect = Rect()

    /**
     * thumb bounds rectF
     */
    private val rectF = RectF()

    var fillColor: ColorStateList? = null
        set(value) {
            if (value != fillColor) {
                field = value
                onStateChange(state)
            }
        }

    var strokeColor: ColorStateList? = null
        set(value) {
            if (value != strokeColor) {
                field = value
                onStateChange(state)
            }
        }

    var shadowColor = Color.TRANSPARENT

    var cornerSize = -1f

    var strokeWidth = 0f
        set(value) {
            field = value
            strokePaint.strokeWidth = value
        }

    var elevation = 0f

    init {
        thumbPaint.apply {
            style = Paint.Style.FILL
        }
        strokePaint.apply {
            style = Paint.Style.STROKE
        }
        shadowPaint.apply {
            color = Color.TRANSPARENT
            isAntiAlias = true
            xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
        }
        updateColorsForState(state)
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
    }

    override fun draw(canvas: Canvas) {
        copyBounds(rect)
        rectF.set(rect)

        if (cornerSize == -1f) {
            cornerSize = rectF.width() / 2f
        }
        // thumb cornerSize and shadow radius
        val radius = min(cornerSize, rectF.width() / 2f)

        //draw shadow
        if (elevation > 0 && shadowColor != Color.TRANSPARENT) {
            shadowPaint.setShadowLayer(calculateElevation(elevation,radius), 0f, 0f, shadowColor)
            canvas.drawRoundRect(rectF, radius, radius, shadowPaint)
        }
        //draw thumb
        canvas.drawRoundRect(rectF, radius, radius, thumbPaint)
        //draw thumb stroke
        if (strokeWidth > 0) {
            canvas.drawRoundRect(rectF, radius, radius, strokePaint)
        }
    }

    override fun isStateful(): Boolean {
        return super.isStateful()
                || (fillColor?.isStateful == true)
                || (strokeColor?.isStateful == true)
    }

    override fun onStateChange(state: IntArray): Boolean {
        val paintColorChanged = updateColorsForState(state)
        val invalidateSelf = paintColorChanged
        return invalidateSelf
    }


    private fun updateColorsForState(state: IntArray): Boolean {
        var invalidateSelf = false
        fillColor?.let {
            val previousFillColor: Int = thumbPaint.color
            val newFillColor: Int = it.getColorForState(state, previousFillColor)
            if (previousFillColor != newFillColor) {
                thumbPaint.color = newFillColor
                invalidateSelf = true
            }
        }
        strokeColor?.let {
            val previousStrokeColor: Int = strokePaint.getColor()
            val newStrokeColor: Int = it.getColorForState(state, previousStrokeColor)
            if (previousStrokeColor != newStrokeColor) {
                strokePaint.color = newStrokeColor
                invalidateSelf = true
            }
        }
        return invalidateSelf
    }

    override fun setAlpha(p0: Int) {

    }

    override fun setColorFilter(p0: ColorFilter?) {

    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }


    /**
     * calculate elevation used for rendering shadow coverage.
     *
     * the maximum value is equal to the radius of the thumb.
     */
    private fun calculateElevation(elevation: Float,radius: Float): Float{
        return min((radius/4 + elevation),radius)
    }
}