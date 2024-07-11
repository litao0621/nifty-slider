package com.litao.slider

import android.content.Context
import android.graphics.Canvas
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.HapticFeedbackConstants
import kotlin.math.roundToInt

/**
 * @author : litao
 * @date   : 2023/2/13 16:25
 */
open class NiftySlider @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    BaseSlider(context, attrs, defStyleAttr) {


    private var valueChangeListener: OnValueChangeListener? = null
    private var intValueChangeListener: OnIntValueChangeListener? = null
    private var sliderTouchListener: OnSliderTouchListener? = null

    private var valueChangeListeners: MutableList<(slider: NiftySlider, value: Float, fromUser: Boolean) -> Unit> = mutableListOf()
    private var intValueChangeListeners: MutableList<(slider: NiftySlider, value: Int, fromUser: Boolean) -> Unit> = mutableListOf()

    private var sliderTouchStartListeners: MutableList<(slider: NiftySlider) -> Unit> = mutableListOf()
    private var sliderTouchStopListeners: MutableList<(slider: NiftySlider) -> Unit> = mutableListOf()

    private var onProgressAnimEndListener: MutableList<(slider: NiftySlider) -> Unit> = mutableListOf()

    var effect: SliderEffect<NiftySlider>? = null

    private var lastChangedValue = -1

    fun interface OnValueChangeListener {
        fun onValueChange(slider: NiftySlider, value: Float, fromUser: Boolean)
    }

    fun interface OnIntValueChangeListener {
        fun onValueChange(slider: NiftySlider, value: Int, fromUser: Boolean)
    }

    interface OnSliderTouchListener {
        fun onStartTrackingTouch(slider: NiftySlider)
        fun onStopTrackingTouch(slider: NiftySlider)
    }

    override fun updateDirtyData() {
        effect?.updateDirtyData()
    }

    override fun onStartTacking() {
        sliderTouchListener?.onStartTrackingTouch(this)
        sliderTouchStartListeners.forEach {
            it.invoke(this)
        }
        effect?.onStartTacking(this)
    }

    override fun onStopTacking() {
        sliderTouchListener?.onStopTrackingTouch(this)
        sliderTouchStopListeners.forEach {
            it.invoke(this)
        }
        effect?.onStopTacking(this)
    }

    override fun onDrawBefore(canvas: Canvas, trackRect: RectF, yCenter: Float) {
        effect?.onDrawBefore(canvas, trackRect, yCenter)
    }

    override fun onDrawAfter(canvas: Canvas, trackRect: RectF, yCenter: Float) {
        effect?.onDrawAfter(canvas, trackRect, yCenter)
    }

    override fun onValueChanged(value: Float, fromUser: Boolean) {
        if (enableHapticFeedback && fromUser && enableStepMode()) {
            performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
        }

        val intValue = value.roundToInt()

        if (lastChangedValue != intValue) {
            lastChangedValue = intValue
            intValueChangeListener?.onValueChange(this, intValue, fromUser)
            intValueChangeListeners.forEach {
                it.invoke(this, intValue, fromUser)
            }
        }

        valueChangeListener?.onValueChange(this, value, fromUser)
        valueChangeListeners.forEach {
            it.invoke(this, value, fromUser)
        }
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

    override fun dispatchDrawSecondaryTrackBefore(canvas: Canvas, trackRect: RectF, yCenter: Float): Boolean {
        return effect?.dispatchDrawSecondaryTrackBefore(this, canvas, trackRect, yCenter) ?: false
    }

    override fun drawTrackAfter(canvas: Canvas, trackRect: RectF, yCenter: Float) {
        effect?.drawTrackAfter(this, canvas, trackRect, yCenter)
    }

    override fun drawSecondaryTrackAfter(canvas: Canvas, trackRect: RectF, yCenter: Float) {
        effect?.drawSecondaryTrackAfter(this, canvas, trackRect, yCenter)
    }

    override fun dispatchDrawIndicatorsBefore(canvas: Canvas, trackRect: RectF, yCenter: Float): Boolean {
        return effect?.dispatchDrawIndicatorsBefore(this, canvas, trackRect, yCenter)?:false
    }

    override fun dispatchDrawIndicatorBefore(canvas: Canvas, trackRect: RectF, indicatorPoint: PointF, index:Int): Boolean {
        return effect?.dispatchDrawIndicatorBefore(this,canvas,trackRect,indicatorPoint,index)?:false
    }

    override fun drawIndicatorAfter(canvas: Canvas, trackRect: RectF, indicatorPoint: PointF, index:Int) {
        effect?.drawIndicatorAfter(this,canvas,trackRect,indicatorPoint,index)
    }

    override fun drawIndicatorsAfter(canvas: Canvas, trackRect: RectF, yCenter: Float) {
        effect?.drawIndicatorsAfter(this, canvas, trackRect, yCenter)
    }

    override fun dispatchDrawThumbBefore(canvas: Canvas, cx: Float, cy: Float): Boolean {
        return effect?.dispatchDrawThumbBefore(this, canvas, cx, cy) ?: false
    }

    override fun drawThumbAfter(canvas: Canvas, cx: Float, cy: Float) {
        effect?.drawThumbAfter(this, canvas, cx, cy)
    }


    /**
     * @deprecated Use [addOnValueChangeListener] instead.
     */
    @Deprecated("use addOnValueChangeListener instead")
    fun setOnValueChangeListener(listener: OnValueChangeListener) {
        this.valueChangeListener = listener
    }

    /**
     * @deprecated Use [addOnIntValueChangeListener] instead.
     */
    @Deprecated("use addOnIntValueChangeListener instead")
    fun setOnIntValueChangeListener(listener: OnIntValueChangeListener) {
        this.intValueChangeListener = listener
    }

    /**
     * @deprecated Use [addOnSliderTouchStartListener],[addOnSliderTouchStopListener] instead.
     */
    @Deprecated("use addOnSliderTouchStartListener | addOnSliderTouchStopListener instead")
    fun setOnSliderTouchListener(listener: OnSliderTouchListener) {
        this.sliderTouchListener = listener
    }

    /**
     * Registers a callback to be invoked when the slider changes.
     * the listener is invoked once for each value.（Float Type）
     *
     * @param listener The callback to run when the slider changes
     */
    fun addOnValueChangeListener(listener: (slider: NiftySlider, value: Float, fromUser: Boolean) -> Unit) {
        valueChangeListeners.add(listener)
    }

    fun removeOnValueChangeListener(listener: (slider: NiftySlider, value: Float, fromUser: Boolean) -> Unit) {
        valueChangeListeners.add(listener)
    }

    fun clearOnValueChangeListener(listener: (slider: NiftySlider, value: Float, fromUser: Boolean) -> Unit) {
        valueChangeListeners.add(listener)
    }


    /**
     * Registers a callback to be invoked when the slider changes.
     * the listener is invoked once for each value.（Integer Type）
     *
     * @param listener The callback to run when the slider changes
     */
    fun addOnIntValueChangeListener(listener: (slider: NiftySlider, value: Int, fromUser: Boolean) -> Unit) {
        intValueChangeListeners.add(listener)
    }

    fun removeOnIntValueChangeListener(listener: (slider: NiftySlider, value: Int, fromUser: Boolean) -> Unit) {
        intValueChangeListeners.add(listener)
    }

    fun clearOnIntValueChangeListener(listener: (slider: NiftySlider, value: Int, fromUser: Boolean) -> Unit) {
        intValueChangeListeners.add(listener)
    }


    /**
     * Registers a callback to be invoked when the slider touch starts.
     * the listener is invoked once for each value.
     *
     * @param listener The callback to run when the slider touch start
     */
    fun addOnSliderTouchStartListener(listener: (slider: NiftySlider) -> Unit) {
        sliderTouchStartListeners.add(listener)
    }

    /**
     * Removes a callback for value changes from this slider touch starts.
     *
     * @param listener The callback that'll stop receive slider touch starts
     */
    fun removeOnSliderTouchStartListener(listener: (slider: NiftySlider) -> Unit) {
        sliderTouchStartListeners.remove(listener)
    }

    /**
     * Removes all instances of touch start listener attached to this slider
     */
    fun clearOnSliderTouchStartListener(listener: (slider: NiftySlider) -> Unit) {
        sliderTouchStartListeners.clear()
    }

    /**
     * Registers a callback to be invoked when the slider touch stops.
     * the listener is invoked once for each value.
     *
     * @param listener The callback to run when the slider touch stops
     */
    fun addOnSliderTouchStopListener(listener: (slider: NiftySlider) -> Unit) {
        sliderTouchStopListeners.add(listener)
    }

    /**
     * Removes a callback for value changes from this slider touch stops.
     *
     * @param listener The callback that'll stop receive slider touch stops
     */
    fun removeOnSliderTouchStopListener(listener: (slider: NiftySlider) -> Unit) {
        sliderTouchStopListeners.remove(listener)
    }

    /**
     * Removes all instances of touch stop listener attached to this slider
     */
    fun clearOnSliderTouchStopListener(listener: (slider: NiftySlider) -> Unit) {
        sliderTouchStopListeners.clear()
    }



    /**
     * Registers a callback to be invoked when the progress changes animation has ended.
     *
     * @param listener The callback to run when the progress changes animation ended
     */
    fun addOnProgressAnimEndListener(listener: (slider: NiftySlider) -> Unit) {
        onProgressAnimEndListener.add(listener)
    }

    fun removeOnProgressAnimEndListener(listener: (slider: NiftySlider) -> Unit) {
        onProgressAnimEndListener.remove(listener)
    }

    fun clearOnProgressAnimEndListener(listener: (slider: NiftySlider) -> Unit) {
        onProgressAnimEndListener.clear()
    }

    /**
     * Invoked when the progress changes animation has ended
     */
    override fun onProgressAnimEnd() {
        super.onProgressAnimEnd()
        onProgressAnimEndListener.forEach {
            it.invoke(this)
        }
    }


}