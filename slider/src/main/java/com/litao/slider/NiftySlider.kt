package com.litao.slider

import android.content.Context
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

    fun interface OnValueChangeListener {
        fun onValueChange(slider: NiftySlider, value: Float, fromUser: Boolean)
    }


    interface OnSliderTouchListener {
        fun onStartTrackingTouch(slider: NiftySlider)
        fun onStopTrackingTouch(slider: NiftySlider)
    }

    override fun onStartTacking() {
        sliderTouchListener?.onStartTrackingTouch(this)
    }

    override fun onStopTacking() {
        sliderTouchListener?.onStopTrackingTouch(this)
    }

    override fun onValueChanged(value: Float, fromUser: Boolean) {
        if (enableHapticFeedback && fromUser && enableStepMode()){
            performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
        }
        valueChangeListener?.onValueChange(this,value,fromUser)
    }



    fun setOnValueChangeListener(listener:OnValueChangeListener){
        this.valueChangeListener = listener
    }

    fun setOnSliderTouchListener(listener:OnSliderTouchListener){
        this.sliderTouchListener = listener
    }


}