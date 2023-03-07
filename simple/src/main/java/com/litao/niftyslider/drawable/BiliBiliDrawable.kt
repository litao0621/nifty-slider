package com.litao.niftyslider.drawable

import android.graphics.*
import android.graphics.Paint.Cap
import android.graphics.drawable.Drawable
import com.litao.niftyslider.dp
import com.litao.slider.anim.SliderValueAnimation

/**
 * @author : litao
 * @date   : 2023/3/7 10:46
 */
class BiliBiliDrawable : Drawable() {

    private val borderColor = 0xFF333333

    private val blinkAnim = SliderValueAnimation()

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    private val fillRect = RectF()
    private var borderWidth = 0f
    private var earHeight = 0f

    private var leftEyeRect = RectF()
    private var rightEyeRect = RectF()

    private var maxEyeOffset = 0f
    private var minEyeOffset = 0f

    private var eyeMaxHeight = 0f
    private var eyeMinHeight = 0f
    private var eyeMaxWidth = 0f
    private var eyeMinWidth = 0f

    private var eyeOffset = 0f
    private var blinkRatio = 1f


    init {
        mPaint.strokeCap = Cap.ROUND
        blinkAnim.apply {
            duration = 500
            addUpdateListener {
                val absValue = getAnimatedValueAbsolute()
                blinkRatio = (1 - 2 * (if (absValue > 0.5f) (1 - absValue) else absValue))
                currentStateValue = -1f
            }
        }
    }


    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        earHeight = bounds.height() * 0.2f
        borderWidth = bounds.height() * 0.1f
        fillRect.set(
            0f,
            earHeight,
            bounds.width().toFloat(),
            bounds.height().toFloat(),
        )
        maxEyeOffset = bounds.width() * 0.15f
        minEyeOffset = -maxEyeOffset

        eyeMaxHeight = fillRect.height() / 3f
        eyeMinHeight = borderWidth / 2f
        eyeMaxWidth = fillRect.width() / 3.5f
        eyeMinWidth = borderWidth
        currentStateValue = -1f
    }


    override fun draw(canvas: Canvas) {
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = borderWidth
        mPaint.color = borderColor.toInt()
        canvas.drawLine(
            bounds.width() / 8f,
            0f,
            bounds.width() * 3 / 8f,
            earHeight * 1.2f,
            mPaint
        )
        canvas.drawLine(
            bounds.width() * 7 / 8f,
            0f,
            bounds.width() * 5 / 8f,
            earHeight * 1.2f,
            mPaint
        )

        mPaint.style = Paint.Style.FILL
        mPaint.color = Color.WHITE
        canvas.drawRoundRect(
            fillRect,
            6f.dp,
            6f.dp,
            mPaint
        )

        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = borderWidth
        mPaint.color = borderColor.toInt()

        canvas.drawRoundRect(
            fillRect,
            6f.dp,
            6f.dp,
            mPaint
        )

        mPaint.style = Paint.Style.FILL
        canvas.drawRoundRect(leftEyeRect, borderWidth / 2, borderWidth / 2, mPaint)
        canvas.drawRoundRect(rightEyeRect, borderWidth / 2, borderWidth / 2, mPaint)
    }

    var currentStateValue = -1f
        set(value) {
            var offset = 0f
            if (value > currentStateValue) {
                //slider to right
                offset = if (eyeOffset < maxEyeOffset) {
                    eyeOffset + 1
                } else {
                    maxEyeOffset
                }
            } else if (value < currentStateValue) {
                //slider to left
                offset = if (eyeOffset > minEyeOffset) {
                    eyeOffset - 1
                } else {
                    minEyeOffset
                }
            }
            field = value

            if (eyeOffset != offset || value == -1f) {
                eyeOffset = offset

                val eyeHeight = eyeMinHeight + (eyeMaxHeight - eyeMinHeight) * blinkRatio

                val eyeWidth = eyeMinWidth + (eyeMaxWidth - eyeMinWidth) * (1 - blinkRatio)

                leftEyeRect.set(
                    bounds.width() / 3f - eyeWidth / 2f + eyeOffset,
                    fillRect.top + fillRect.height() / 2 - eyeHeight / 2,
                    bounds.width() / 3f + eyeWidth / 2f + +eyeOffset,
                    fillRect.top + fillRect.height() / 2 + eyeHeight / 2,
                )

                rightEyeRect.set(
                    bounds.width() * 2 / 3f - eyeWidth / 2f + eyeOffset,
                    fillRect.top + fillRect.height() / 2 - eyeHeight / 2,
                    bounds.width() * 2 / 3f + eyeWidth / 2f + eyeOffset,
                    fillRect.top + fillRect.height() / 2 + eyeHeight / 2,
                )
                invalidateSelf()
            }
        }

    fun startBlinkAnim() {
        blinkAnim.start()
    }

    override fun setAlpha(p0: Int) {

    }

    override fun setColorFilter(p0: ColorFilter?) {

    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }
}