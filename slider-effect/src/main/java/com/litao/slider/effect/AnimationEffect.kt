package com.litao.slider.effect

import android.animation.TimeInterpolator
import android.content.res.ColorStateList
import androidx.annotation.ColorInt
import androidx.core.animation.doOnEnd
import androidx.core.graphics.ColorUtils
import androidx.core.view.ViewCompat
import com.litao.slider.NiftySlider
import com.litao.slider.anim.SliderValueAnimation

/**
 * @author : litao
 * @date   : 2023/3/6 10:40
 */
class AnimationEffect(private val slider: NiftySlider) : BaseEffect() {

    private val sliderAnimation = SliderValueAnimation()

    var animDuration = 500L
        set(value) {
            field = value
            sliderAnimation.duration = animDuration
        }

    var endAnimDelay = 0L

    var srcTrackHeight = UNSET
    var srcThumbRadius = UNSET
    var srcThumbWidth = UNSET
    var srcThumbHeight = UNSET
    var srcThumbColor = UNSET_COLOR
    var srcTrackColor = UNSET_COLOR
    var srcInactiveTrackColor = UNSET_COLOR

    var targetTrackHeight = UNSET
    var targetThumbRadius = UNSET
    var targetThumbWidth = UNSET
    var targetThumbHeight = UNSET
    var targetThumbColor = UNSET_COLOR
    var targetTrackColor = UNSET_COLOR
    var targetInactiveTrackColor = UNSET_COLOR

    var animationListener: OnAnimationChangeListener? = null


    companion object {
        const val UNSET = -1
        const val UNSET_COLOR = Int.MAX_VALUE
    }


    interface OnAnimationChangeListener {
        fun onEnd(slider: NiftySlider)
    }


    init {
        sliderAnimation.apply {
            addUpdateListener { animationUpdate(getAnimatedValueAbsolute()) }
            doOnEnd {
                if (isReversed()) {
                    if (ViewCompat.isAttachedToWindow(slider)) {
                        animationListener?.onEnd(slider)
                    }
                }
            }
        }

    }

    override fun onStartTacking(slider: NiftySlider) {
        super.onStartTacking(slider)
        startAnim(false)
    }


    override fun onStopTacking(slider: NiftySlider) {
        super.onStopTacking(slider)
        startAnim(true)
    }


    fun startAnim(isReversed: Boolean) {
        sliderAnimation.apply {
            if (isReversed) {
                if (endAnimDelay > 0) {
                    slider.postDelayed({
                        if (ViewCompat.isAttachedToWindow(slider)) {
                            reverse()
                        }
                    }, endAnimDelay)
                } else {
                    reverse()
                }
            } else {
                start()
            }
        }
    }

    /**
     * update slider animation
     */
    fun animationUpdate(value: Float) {
        updateTrackHeight(value)
        updateThumbSize(value)
        updateThumbColor(value)
        updateTrackColor(value)
        updateInactiveTrackColor(value)
        slider.postInvalidate()
    }

    /**
     * update track height with animation
     */
    private fun updateTrackHeight(value: Float) {
        if (targetTrackHeight != UNSET) {
            val height = getValueByFraction(srcTrackHeight, targetTrackHeight, value).toInt()
            slider.trackHeight = height
        }
    }

    /**
     * update thumb size with animation
     */
    private fun updateThumbSize(value: Float) {
        if (targetThumbWidth != UNSET || targetThumbHeight != UNSET) {
            val h = if (targetThumbHeight >= 0) {
                getValueByFraction(srcThumbHeight, targetThumbHeight, value).toInt()
            } else {
                srcThumbHeight
            }

            val w = if (targetThumbWidth >= 0) {
                getValueByFraction(srcThumbWidth, targetThumbWidth, value).toInt()
            } else {
                srcThumbWidth
            }

            val r = if (targetThumbRadius >= 0) {
                getValueByFraction(srcThumbRadius, targetThumbRadius, value).toInt()
            } else {
                srcThumbRadius
            }
            slider.setThumbWidthAndHeight(w, h, r)
        } else if (targetThumbRadius != UNSET) {
            slider.thumbRadius = getValueByFraction(srcThumbRadius, targetThumbRadius, value).toInt()
        }
    }

    /**
     * update thumb color with animation
     */
    private fun updateThumbColor(value: Float) {
        if (targetThumbColor != UNSET_COLOR) {
            slider.setThumbTintList(ColorStateList.valueOf(getColorByFraction(srcThumbColor, targetThumbColor, value)))
        }
    }

    /**
     * update track color with animation
     */
    private fun updateTrackColor(value: Float) {
        if (targetTrackColor != UNSET_COLOR) {
            slider.setTrackTintList(ColorStateList.valueOf(getColorByFraction(srcTrackColor, targetTrackColor, value)))
        }
    }

    /**
     * update inactive track color with animation
     */
    private fun updateInactiveTrackColor(value: Float) {
        if (targetInactiveTrackColor != UNSET_COLOR) {
            slider.setTrackInactiveTintList(
                ColorStateList.valueOf(
                    getColorByFraction(
                        srcInactiveTrackColor,
                        targetInactiveTrackColor,
                        value
                    )
                )
            )
        }
    }


    /**
     * Sets the interpolator of this animation effect
     */
    fun setInterpolator(interpolator: TimeInterpolator) {
        sliderAnimation.interpolator = interpolator
    }


    private fun getValueByFraction(srcValue: Int, targetValue: Int, fraction: Float): Float {
        return srcValue + ((targetValue - srcValue) * fraction)
    }

    @ColorInt
    private fun getColorByFraction(srcColor: Int, targetColor: Int, fraction: Float): Int {
        return ColorUtils.blendARGB(srcColor, targetColor, fraction)
    }


}