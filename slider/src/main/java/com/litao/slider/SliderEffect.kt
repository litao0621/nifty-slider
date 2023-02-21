package com.litao.slider

import android.graphics.Canvas
import android.graphics.RectF

/**
 * @author : litao
 * @date   : 2023/2/21 14:10
 */
interface SliderEffect<T : BaseSlider> {

    fun onStartTacking(slider: T)
    fun onStopTacking(slider: T)

    fun onValueChanged(slider: T, value: Float, fromUser: Boolean)

    fun dispatchDrawInactiveTrackBefore(slider: T, canvas: Canvas, trackRect: RectF, yCenter: Float): Boolean
    fun drawInactiveTrackAfter(slider: T, canvas: Canvas, trackRect: RectF, yCenter: Float)

    fun dispatchDrawTrackBefore(slider: T, canvas: Canvas, trackRect: RectF, yCenter: Float): Boolean
    fun drawTrackAfter(slider: T, canvas: Canvas, trackRect: RectF, yCenter: Float)

    fun dispatchDrawThumbBefore(slider: T, canvas: Canvas, cx: Float, cy: Float): Boolean
    fun drawThumbAfter(slider: T, canvas: Canvas, cx: Float, cy: Float)

}