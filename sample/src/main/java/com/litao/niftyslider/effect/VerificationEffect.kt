package com.litao.niftyslider.effect

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import androidx.annotation.ColorInt
import com.litao.slider.NiftySlider
import com.litao.slider.effect.BaseEffect

/**
 * @author : litao
 * @date   : 2024/8/19 12:55
 */
class VerificationEffect(private val slider: NiftySlider) : BaseEffect() {

    var text:String? = null

    @ColorInt var textColor = Color.BLACK

    var textSize = 24f


    private var textPaint: Paint = Paint(HIGH_QUALITY_FLAGS).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
    }

    override fun drawInactiveTrackAfter(
        slider: NiftySlider,
        canvas: Canvas,
        trackRect: RectF,
        trackCenter: Float) {
        text?.let {
            textPaint.apply {
                setColor(textColor)
                this.textSize = this@VerificationEffect.textSize
            }
            canvas.drawText(
                it,
                trackRect.left + trackRect.width()/2,
                baseline(yCenter = trackCenter),
                textPaint
            )
        }

    }

    private fun baseline(yCenter: Float) = yCenter - (textPaint.fontMetricsInt.bottom + textPaint.fontMetricsInt.top) / 2


}