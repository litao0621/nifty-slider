package com.litao.slider.effect

import android.graphics.*
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.core.graphics.ColorUtils
import com.litao.slider.NiftySlider


/**
 * @author : litao
 * @date   : 2023/2/28 17:16
 */
class ColorPickEffect(private val slider: NiftySlider) : BaseEffect() {

    private val colorTrackPaint = Paint(HIGH_QUALITY_FLAGS)
    private var colorShader:LinearGradient? = null
    private var colors = DEFAULT_COLORS

    var colorValueChangeListener:OnColorValueChangeListener? = null


    fun interface OnColorValueChangeListener {
        fun onColorValueChange(slider: NiftySlider, @ColorInt color: Int, fromUser: Boolean)
    }

    init {
        colorTrackPaint.style = Paint.Style.FILL
    }


    companion object{
        val DEFAULT_COLORS:IntArray = intArrayOf(
            0xFFFF0000.toInt(),
            0xFFFF00FF.toInt(),
            0xFF0000FF.toInt(),
            0xFF00FFFF.toInt(),
            0xFF00FF00.toInt(),
            0xFFFFFF00.toInt(),
            0xFFFF0000.toInt(),
        )
    }


    override fun onValueChanged(slider: NiftySlider, value: Float, fromUser: Boolean) {
        super.onValueChanged(slider, value, fromUser)
        colorValueChangeListener?.onColorValueChange(slider,calculateColor(slider.percentValue()),fromUser)
    }


    override fun dispatchDrawTrackBefore(
        slider: NiftySlider,
        canvas: Canvas,
        trackRect: RectF,
        yCenter: Float
    ): Boolean {
        return true
    }


    override fun dispatchDrawInactiveTrackBefore(
        slider: NiftySlider,
        canvas: Canvas,
        trackRect: RectF,
        yCenter: Float
    ): Boolean {
        maybeCreateShader(trackRect)

        canvas.drawRoundRect(
            trackRect,
            trackRect.height() / 2f,
            trackRect.height() / 2f,
            colorTrackPaint
        )

        return true
    }

    private fun maybeCreateShader(trackRect: RectF){
        if (colorShader == null){
            colorShader = LinearGradient(
                0f,
                0f,
                trackRect.width(),
                0f,
                colors,
                null,
                Shader.TileMode.CLAMP
                )

            colorTrackPaint.shader = colorShader
        }
    }


    @ColorInt
    private fun calculateColor(@FloatRange(from = 0.0, to = 1.0) value: Float): Int {

        val colorsLength = colors.size
        val colorsRangeLength = colorsLength - 1
        val stepValue = 1f / colorsRangeLength

        val index = (value / stepValue).toInt()
        val rangeValue = value % stepValue
        val colorRatio = rangeValue/ stepValue

        if (index >= colorsRangeLength){
            return colors[index]
        }else{
            val startColor = colors[index]
            val endColor = colors[index + 1]
            return ColorUtils.blendARGB(startColor,endColor,colorRatio)
        }
    }

    /**
     * update gradient colors
     */
    fun updateColors(colors:IntArray){
        this.colors = colors
        colorShader = null
        slider.invalidate()
    }

}