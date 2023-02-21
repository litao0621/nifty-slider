package com.litao.slider

import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.HapticFeedbackConstants

/**
 * @author : litao
 * @date   : 2023/2/13 16:25
 */
open class NiftySlider @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    BaseSlider(context, attrs, defStyleAttr) {


    private var valueChangeListener: OnValueChangeListener? = null
    private var sliderTouchListener: OnSliderTouchListener? = null

    var effect: SliderEffect<NiftySlider>? = null

    fun interface OnValueChangeListener {
        fun onValueChange(slider: NiftySlider, value: Float, fromUser: Boolean)
    }


    interface OnSliderTouchListener {
        fun onStartTrackingTouch(slider: NiftySlider)
        fun onStopTrackingTouch(slider: NiftySlider)
    }

    override fun onStartTacking() {
        sliderTouchListener?.onStartTrackingTouch(this)
        effect?.onStartTacking(this)
    }

    override fun onStopTacking() {
        sliderTouchListener?.onStopTrackingTouch(this)
        effect?.onStopTacking(this)
    }

    override fun onValueChanged(value: Float, fromUser: Boolean) {
        if (enableHapticFeedback && fromUser && enableStepMode()) {
            performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
        }
        valueChangeListener?.onValueChange(this, value, fromUser)
        effect?.onValueChanged(this, value, fromUser)
    }

    override fun dispatchDrawInactiveTrackBefore(canvas: Canvas, trackRect: RectF, yCenter: Float): Boolean {
        return effect?.dispatchDrawInactiveTrackBefore(this, canvas, trackRect, yCenter) ?: false
    }

    override fun drawInactiveTrackAfter(canvas: Canvas, trackRect: RectF, yCenter: Float) {
        effect?.drawInactiveTrackAfter(this, canvas, trackRect, yCenter)
    }

    override fun dispatchDrawTrackBefore(canvas: Canvas, trackRect: RectF, yCenter: Float): Boolean {
        return effect?.dispatchDrawTrackBefore(this, canvas, trackRect, yCenter) ?: false
    }

    override fun drawTrackAfter(canvas: Canvas, trackRect: RectF, yCenter: Float) {
        effect?.drawTrackAfter(this, canvas, trackRect, yCenter)
    }

    override fun dispatchDrawThumbBefore(canvas: Canvas, cx: Float, cy: Float): Boolean {
        return effect?.dispatchDrawThumbBefore(this, canvas, cx, cy) ?: false
    }

    override fun drawThumbAfter(canvas: Canvas, cx: Float, cy: Float) {
        effect?.drawThumbAfter(this, canvas, cx, cy)
    }


    fun setOnValueChangeListener(listener: OnValueChangeListener) {
        this.valueChangeListener = listener
    }

    fun setOnSliderTouchListener(listener: OnSliderTouchListener) {
        this.sliderTouchListener = listener
    }


}