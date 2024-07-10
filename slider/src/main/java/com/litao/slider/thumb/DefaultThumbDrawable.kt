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
import com.litao.slider.Utils
import kotlin.math.min


/**
 * @author : litao
 * @date   : 2023/8/14 11:43
 */
class DefaultThumbDrawable : Drawable(), IBaseThumbDrawable {

    private val thumbPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)

    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)

    private val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)

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
        // Offset the shadow inward by 1dp . prevent the appearance of rough edges around the thumb.
        val shadowOffset = Utils.dpToPx(1).toFloat()
        var hasShadow = false
        //draw shadow
        if (isSupportShadow()) {
            hasShadow = true
            shadowPaint.color = shadowColor
            shadowPaint.setShadowLayer(calculateElevation(elevation + shadowOffset,radius), 0f, 0f, shadowColor)
            rectF.inset(shadowOffset,shadowOffset)
            canvas.drawRoundRect(rectF, radius, radius, shadowPaint)
        }

        if (hasShadow) {
            rectF.inset(-shadowOffset, -shadowOffset)
        }
        //draw thumb
        canvas.drawRoundRect(rectF, radius, radius, thumbPaint)
        //draw thumb stroke
        if (isSupportStroke()) {
            canvas.drawRoundRect(rectF, radius, radius, strokePaint)
        }
    }


    /**
     * If the drawable size is too small, it may cause display issues.
     * Therefore, the minimum size for displaying shadows has been controlled.
     */
    private fun isSupportShadow(): Boolean {
        return elevation > 0
                && shadowColor != Color.TRANSPARENT
                && rectF.width() > Utils.dpToPx(3)
                && rectF.height() > Utils.dpToPx(3)
    }

    private fun isSupportStroke(): Boolean {
        return strokeWidth > 0
                && rectF.width() > strokeWidth * 2
                && rectF.height() > strokeWidth * 2
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
        return min(elevation,radius)
    }
}