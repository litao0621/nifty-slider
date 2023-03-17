package com.litao.slider.anim

import android.animation.ValueAnimator
import android.os.Handler
import android.os.Looper
import androidx.annotation.FloatRange

/**
 * @author : litao
 * @date   : 2023/3/17 14:21
 */
class ThumbValueAnimation : ValueAnimator() {

    private var isThumbHidden = false

    private val handler = Handler(Looper.getMainLooper())

    companion object {
        const val DEFAULT_DURATION = 300L
    }


    init {
        setFloatValues(1f, 0f)
        duration = DEFAULT_DURATION
    }


    override fun getAnimatedValue(): Any {
        return super.getAnimatedValue()
    }

    @FloatRange(from = 0.0, to = 1.0)
    fun getAnimatedValueAbsolute(): Float {
        return animatedValue.toString().toFloat()
    }

    fun hide(animated:Boolean = true,delayMillis:Long = 0){
        handler.removeCallbacksAndMessages(null)
        if (isRunning){
            end()
        }
        if (!isThumbHidden) {
            handler.postDelayed({ executeHide(animated) },delayMillis)
        } else {
            cancel()
        }
    }


    fun show(animated:Boolean = true,delayMillis:Long = 0){
        handler.removeCallbacksAndMessages(null)
        if (isRunning){
            end()
        }
        if (isThumbHidden) {
            handler.postDelayed({ executeShow(animated) },delayMillis)
        }else{
            cancel()
        }

    }

    fun toggle(animated:Boolean = true){
        if (isThumbHidden){
            show(animated)
        }else{
            hide(animated)
        }
    }


    private fun executeHide(animated:Boolean){
        isThumbHidden = true
        if (animated) {
            super.start()
        } else {
            currentPlayTime = duration
        }
    }

    private fun executeShow(animated:Boolean){
        isThumbHidden = false
        if (animated) {
            super.reverse()
        } else {
            currentPlayTime = 0
        }
    }


    fun isThumbHidden(): Boolean {
        return isThumbHidden && !isRunning
    }


}