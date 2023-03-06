package com.litao.slider.anim

import android.animation.ValueAnimator
import androidx.annotation.FloatRange

/**
 * @author : litao
 * @date   : 2023/3/6 16:01
 */
class SliderValueAnimation : ValueAnimator() {


    private var isAnimReversed = false

    companion object {
        const val DEFAULT_DURATION = 500L
    }


    init {
        setFloatValues(0f, 1f)
        duration = DEFAULT_DURATION
    }


    override fun getAnimatedValue(): Any {
        return super.getAnimatedValue()
    }

    @FloatRange(from = 0.0, to = 1.0)
    fun getAnimatedValueAbsolute(): Float {
        return animatedValue.toString().toFloat()
    }

    override fun start() {
        isAnimReversed = false
        super.start()
    }

    override fun reverse() {
        isAnimReversed = true
        super.reverse()
    }

    fun isReversed(): Boolean {
        return isAnimReversed
    }


}