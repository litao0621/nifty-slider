package com.litao.slider

import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.RectF

/**
 * @author : litao
 * @date   : 2023/2/21 14:10
 */
interface SliderEffect<T : BaseSlider> {

    /** Called when a slider's touch event is being started */
    fun onStartTacking(slider: T)

    /** Called when a slider's touch event is being stopped */
    fun onStopTacking(slider: T)

    /** Called when the value of the slider changes  */
    fun onValueChanged(slider: T, value: Float, fromUser: Boolean)

    /** Called before slider onDraw */
    fun onDrawBefore(canvas: Canvas,trackRect: RectF, yCenter: Float)

    /** Called after slider onDraw */
    fun onDrawAfter(canvas: Canvas,trackRect: RectF, yCenter: Float)

    /** Called before draw inactive track . Return true if the interrupt default draw */
    fun dispatchDrawInactiveTrackBefore(slider: T, canvas: Canvas, trackRect: RectF, yCenter: Float): Boolean
    /** Called after draw inactive track */
    fun drawInactiveTrackAfter(slider: T, canvas: Canvas, trackRect: RectF, yCenter: Float)

    /** Called before draw active track . Return true if the interrupt default draw*/
    fun dispatchDrawTrackBefore(slider: T, canvas: Canvas, trackRect: RectF,inactiveTrackRect: RectF, yCenter: Float): Boolean
    /** Called after draw active track */
    fun drawTrackAfter(slider: T, canvas: Canvas, trackRect: RectF, inactiveTrackRect: RectF,yCenter: Float)

    /** Called before draw secondary track . Return true if the interrupt default draw*/
    fun dispatchDrawSecondaryTrackBefore(slider: T, canvas: Canvas, trackRect: RectF,inactiveTrackRect: RectF, yCenter: Float): Boolean
    /** Called after draw secondary track */
    fun drawSecondaryTrackAfter(slider: T, canvas: Canvas, trackRect: RectF,inactiveTrackRect: RectF, yCenter: Float)

    /** Called before draw thumb drawable . Return true if the interrupt default draw*/
    fun dispatchDrawThumbBefore(slider: T, canvas: Canvas, cx: Float, cy: Float): Boolean
    /** Called after draw thumb drawable */
    fun drawThumbAfter(slider: T, canvas: Canvas, cx: Float, cy: Float)

    /** Called before draw indicators . Return true if the interrupt default draw*/
    fun dispatchDrawIndicatorsBefore(slider: T, canvas: Canvas, trackRect: RectF, yCenter: Float): Boolean
    /** Called before draw single indicator . Return true if the interrupt default draw*/
    fun dispatchDrawIndicatorBefore(slider: T, canvas: Canvas, trackRect: RectF, indicatorPoint: PointF, index:Int): Boolean
    /** Called after draw single indicator */
    fun drawIndicatorAfter(slider: T, canvas: Canvas, trackRect: RectF, indicatorPoint: PointF, index:Int)
    /** Called after draw indicators */
    fun drawIndicatorsAfter(slider: T, canvas: Canvas, trackRect: RectF, yCenter: Float)

    /** The parameter has changed, and there is dirty data */
    fun updateDirtyData()

}