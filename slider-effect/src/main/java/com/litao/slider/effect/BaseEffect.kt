package com.litao.slider.effect

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import com.litao.slider.NiftySlider
import com.litao.slider.SliderEffect

/**
 * @author : litao
 * @date   : 2023/2/21 15:18
 */
open class BaseEffect : SliderEffect<NiftySlider> {

    companion object{
        const val HIGH_QUALITY_FLAGS = Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG
    }

    override fun onStartTacking(slider: NiftySlider) {
    }

    override fun onStopTacking(slider: NiftySlider) {
    }

    override fun onValueChanged(slider: NiftySlider, value: Float, fromUser: Boolean) {
    }

    override fun dispatchDrawInactiveTrackBefore(slider: NiftySlider, canvas: Canvas, trackRect: RectF, yCenter: Float): Boolean {
        return false
    }

    override fun drawInactiveTrackAfter(slider: NiftySlider,canvas: Canvas,  trackRect: RectF, yCenter: Float) {
    }

    override fun dispatchDrawTrackBefore(slider: NiftySlider, canvas: Canvas, trackRect: RectF, yCenter: Float): Boolean {
        return false
    }

    override fun drawTrackAfter(slider: NiftySlider, canvas: Canvas, trackRect: RectF, yCenter: Float) {
    }

    override fun dispatchDrawThumbBefore(slider: NiftySlider, canvas: Canvas, cx: Float, cy: Float): Boolean {
        return false
    }

    override fun drawThumbAfter(slider: NiftySlider, canvas: Canvas, cx: Float, cy: Float) {
    }

    override fun dispatchDrawSecondaryTrackBefore(
        slider: NiftySlider,
        canvas: Canvas,
        trackRect: RectF,
        yCenter: Float
    ): Boolean {
        return false
    }

    override fun drawSecondaryTrackAfter(slider: NiftySlider, canvas: Canvas, trackRect: RectF, yCenter: Float) {
    }

    override fun onDrawBefore(canvas: Canvas, trackRect: RectF, yCenter: Float) {
    }

    override fun onDrawAfter(canvas: Canvas, trackRect: RectF, yCenter: Float) {
    }

    override fun drawIndicatorsAfter(slider: NiftySlider, canvas: Canvas, trackRect: RectF, yCenter: Float) {
    }

    override fun drawIndicatorAfter(slider: NiftySlider, canvas: Canvas, trackRect: RectF, indicatorPoint: PointF, index:Int) {
    }

    override fun dispatchDrawIndicatorBefore(
        slider: NiftySlider,
        canvas: Canvas,
        trackRect: RectF,
        indicatorPoint: PointF,
        index:Int
    ): Boolean {
        return false
    }

    override fun dispatchDrawIndicatorsBefore(
        slider: NiftySlider,
        canvas: Canvas,
        trackRect: RectF,
        yCenter: Float
    ): Boolean {
        return false
    }

    override fun updateDirtyData() {

    }

}