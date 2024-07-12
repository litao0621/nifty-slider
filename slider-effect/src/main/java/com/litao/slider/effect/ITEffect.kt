package com.litao.slider.effect

import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.withTranslation
import com.litao.slider.NiftySlider

/**
 * @author : litao
 * @date   : 2023/2/21 15:29
 */
class ITEffect(private val slider: NiftySlider) : BaseEffect() {

    private var startTextBounds = Rect()
    private var endTextBounds = Rect()

    private var textPaint: Paint = Paint(HIGH_QUALITY_FLAGS).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
    }


    /**
     * Slider track 绘制结束后的额外绘制
     */
    override fun drawTrackAfter(slider: NiftySlider, canvas: Canvas, trackRect: RectF,inactiveTrackRect:RectF, yCenter: Float) {
        drawStartIcon(canvas,inactiveTrackRect,yCenter)
        drawEndIcon(canvas,inactiveTrackRect,yCenter)
        drawStartText(canvas,inactiveTrackRect,yCenter)
        drawEndText(canvas,inactiveTrackRect,yCenter)
    }

    /**
     * 绘制Slider起始位置图标
     */
    private fun drawStartIcon(canvas: Canvas, trackRect: RectF, yCenter: Float){
        getStartIconInRtl()?.let {
            canvas.withTranslation(
                trackRect.left + startPadding,
                yCenter - it.bounds.height() / 2f
            ) {
                it.draw(this)
            }
        }
    }

    /**
     * 绘制Slider结束位置图标
     */
    private fun drawEndIcon(canvas: Canvas, trackRect: RectF, yCenter: Float){
        getEndIconInRtl()?.let {
            canvas.withTranslation(
                slider.trackWidth + trackRect.left - endPadding - it.bounds.width(),
                yCenter - it.bounds.height() / 2f
            ) {
                it.draw(this)
            }
        }
    }

    /**
     * 绘制Slider起始位置文本
     */
    private fun drawStartText(canvas: Canvas, trackRect: RectF, yCenter: Float){
        getStartTextInRtl()?.let {
            startTintList?.let { colorList ->
                textPaint.color = slider.getColorForState(colorList)
            }
            textPaint.textSize = startTextSize
            textPaint.getTextBounds(it,0,it.length,startTextBounds)
            val baseline = baseline(yCenter)
            canvas.drawText(
                it,
                trackRect.left + startPadding + startTextBounds.width()/2,
                baseline,
                textPaint
            )
        }
    }

    /**
     * 绘制Slider结束位置文本
     */
    private fun drawEndText(canvas: Canvas, trackRect: RectF, yCenter: Float){
        getEndTextInRtl()?.let {
            endTintList?.let { colorList ->
                textPaint.color = slider.getColorForState(colorList)
            }
            textPaint.textSize = endTextSize
            textPaint.getTextBounds(it,0,it.length,endTextBounds)
            val baseline = baseline(yCenter)
            canvas.drawText(
                it,
                slider.trackWidth.toFloat() + trackRect.left - endPadding - endTextBounds.width()/2,
                baseline,
                textPaint
            )
        }
    }



    fun setStartIcon(@DrawableRes drawableResId: Int) {
        val originalDrawable = ContextCompat.getDrawable(slider.context, drawableResId)
        startIcon = originalDrawable
    }

    fun setEndIcon(@DrawableRes drawableResId: Int) {
        val originalDrawable = ContextCompat.getDrawable(slider.context,drawableResId)
        endIcon = originalDrawable
    }

    /**
     * 起始位置文本
     */
    var startText: String? = null
        set(value) {
            if (value != null) {
                field = value
                slider.postInvalidate()
            }
        }

    /**
     * 结束位置文本
     */
    var endText: String? = null
        set(value) {
            if (value != null) {
                field = value
                slider.postInvalidate()
            }
        }


    /**
     * 起始位置文本大小
     */
    var startTextSize = 20f
        set(value) {
            field = value
            slider.postInvalidate()
        }

    /**
     * 结束位置文本大小
     */
    var endTextSize = 20f
        set(value) {
            field = value
            slider.postInvalidate()
        }


    /**
     * 起始位置icon
     */
    var startIcon: Drawable? = null
        set(value) {
            if (value != null) {
                field = initializeCustomIconDrawable(value)?.apply {
                    DrawableCompat.setTintList(this,startTintList)
                    if (startIconSize > 0) {
                        setBounds(0, 0, startIconSize, startIconSize)
                    }
                }
                slider.postInvalidate()
            }
        }

    /**
     * 结束位置icon
     */
    var endIcon: Drawable? = null
        set(value) {
            if (value != null) {
                field = initializeCustomIconDrawable(value)?.apply {
                    DrawableCompat.setTintList(this,endTintList)
                    if (endIconSize > 0) {
                        setBounds(0, 0, endIconSize, endIconSize)
                    }
                }
                slider.postInvalidate()
            }
        }

    /**
     * 起始装饰内容着色
     */
    var startTintList:ColorStateList? = null
        set(value) {
            field = value
            startIcon?.let {
                DrawableCompat.setTintList(it,value)
            }

        }

    /**
     * 结束装饰内容着色
     */
    var endTintList:ColorStateList? = null
        set(value) {
            field = value
            endIcon?.let {
                DrawableCompat.setTintList(it,value)
            }

        }

    /**
     * 起始icon大小
     */
    var startIconSize = 0
        set(value) {
            field = value
            startIcon?.setBounds(0, 0, value, value)
            slider.postInvalidate()
        }

    /**
     * 结束icon大小
     */
    var endIconSize = 0
        set(value) {
            field = value
            endIcon?.setBounds(0, 0, value, value)
            slider.postInvalidate()
        }

    /**
     * 起始装饰padding值
     */
    var startPadding = 0
        set(value) {
            field = value
            slider.postInvalidate()
        }

    /**
     * 结束装饰padding值
     */
    var endPadding = 0
        set(value) {
            field = value
            slider.postInvalidate()
        }

    private fun initializeCustomIconDrawable(originalDrawable: Drawable): Drawable? {
        return originalDrawable.mutate().constantState?.newDrawable()
    }

    private fun baseline(yCenter: Float) = yCenter - (textPaint.fontMetricsInt.bottom + textPaint.fontMetricsInt.top) / 2


    private fun getStartIconInRtl(): Drawable? {
        return if (slider.isRtl()) {
            endIcon
        } else {
            startIcon
        }
    }

    private fun getEndIconInRtl(): Drawable? {
        return if (slider.isRtl()) {
            startIcon
        } else {
            endIcon
        }
    }

    private fun getStartTextInRtl(): String? {
        return if (slider.isRtl()) {
            endText
        } else {
            startText
        }
    }

    private fun getEndTextInRtl(): String? {
        return if (slider.isRtl()) {
            startText
        } else {
            endText
        }
    }



}