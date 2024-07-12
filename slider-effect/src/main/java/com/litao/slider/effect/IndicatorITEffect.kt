package com.litao.slider.effect

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.FontMetrics
import android.graphics.PointF
import android.graphics.RectF
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.withTranslation
import androidx.core.view.doOnAttach
import com.litao.slider.NiftySlider
import com.litao.slider.Utils
import com.litao.slider.model.IndicatorIcon
import com.litao.slider.model.IndicatorText


/**
 * @author : litao
 * @date   : 2024/7/11 13:34
 */
class IndicatorITEffect(private val slider: NiftySlider) : BaseEffect() {
    private val indicatorPaint = Paint(HIGH_QUALITY_FLAGS)
    private var drawPaint = Paint(HIGH_QUALITY_FLAGS)

    var indicatorWidth = Utils.dpToPx(3).toFloat()
        set(value) {
            indicatorPaint.strokeWidth = value
            field = value
        }

    var indicatorHeight = Utils.dpToPx(16).toFloat()

    var drawSpace = Utils.dpToPx(12)


    var iconDrawArray = mutableListOf<IndicatorIcon>()
    var textDrawArray = mutableListOf<IndicatorText>()


    init {
        indicatorPaint.apply {
            strokeWidth = indicatorWidth
            style = Paint.Style.FILL
            color = Color.parseColor("#b0bed5")
        }
        slider.doOnAttach {
            if (slider.isRtl()) {
                iconDrawArray.reverse()
                textDrawArray.reverse()
            }
        }
    }


    override fun dispatchDrawIndicatorBefore(
        slider: NiftySlider,
        canvas: Canvas,
        trackRect: RectF,
        indicatorPoint: PointF,
        index: Int
    ): Boolean {
        canvas.drawLine(
            indicatorPoint.x,
            indicatorPoint.y  - indicatorHeight/2,
            indicatorPoint.x,
            indicatorPoint.y  + indicatorHeight/2,
            indicatorPaint
        )

        if (iconDrawArray.isNotEmpty()){
            drawIcon(index,canvas,indicatorPoint)
        }else if (textDrawArray.isNotEmpty()){
            drawText(index,canvas,indicatorPoint)
        }


        return true
    }


    private fun drawIcon(
        index:Int,
        canvas: Canvas,
        centrePoint: PointF
    ){
        val model = iconDrawArray.getOrNull(index)

        if (model != null){
            val drawable = model.icon
            val size = model.size
            val tintColor = model.tintColor

            if (drawable != null){
                val tempDrawable = drawable.mutate()

                if (tintColor != -1){
                    DrawableCompat.setTint(tempDrawable,tintColor)
                }

                tempDrawable.setBounds(0,0,size,size)
                canvas.withTranslation(centrePoint.x - size/2, centrePoint.y - size - indicatorHeight/2 - drawSpace) {
                    tempDrawable.draw(canvas)
                }

            }
        }

    }


    private fun drawText(
        index:Int,
        canvas: Canvas,
        centrePoint: PointF
    ){
        val model = textDrawArray.getOrNull(index)
        if (model != null) {
            val text = model.text
            val color = model.fontColor
            val fontSize = model.fontSize


            drawPaint.apply {
                this.color = color
                this.textSize = fontSize
                this.textAlign = Paint.Align.CENTER
            }
            val fontMetrics: FontMetrics = drawPaint.getFontMetrics()

            canvas.drawText(
                text,
                centrePoint.x,
                centrePoint.y - indicatorHeight/2 - fontMetrics.descent - drawSpace,
                drawPaint
            )
        }
    }




}