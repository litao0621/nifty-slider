package com.litao.slider.anim

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.os.Build
import android.util.ArrayMap
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.transition.TransitionValues
import androidx.transition.Visibility
import kotlin.math.max


/**
 * @author : litao
 * @date   : 2023/3/22 19:30
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class RevealTransition() : Visibility(), ITipTransition {
    var centerX = 0f
    var centerY = 0f
    var tipViewBottomY = 0f
    var sliderViewY = 0f

    init {
        centerX = DEFAULT_CENTER
        centerY = DEFAULT_CENTER
    }


    companion object {
        private const val TAG = "RevealTransition"
        private const val DEFAULT_CENTER = 0.5f
    }

    override fun onAppear(
        sceneRoot: ViewGroup?, view: View, startValues: TransitionValues?, endValues: TransitionValues?
    ): Animator {
        val width = view.width
        val height = view.height
        val cx = (width * centerX).toInt()
        val cy = (height * centerY).toInt()
        val distance: Int = if (width > height) {
            max(width - cx, cx)
        } else {
            max(height - cy, cy)
        }

        val fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
        val slideAnim = ObjectAnimator.ofFloat(
            view,
            "y",
            sliderViewY,
            tipViewBottomY - height
        )
        val revealAnim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, distance.toFloat())
        slideAnim.duration = revealAnim.duration

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(fadeAnim, slideAnim, revealAnim)

        return NoPauseAnimator(animatorSet)
    }

    override fun onDisappear(
        sceneRoot: ViewGroup?, view: View, startValues: TransitionValues?, endValues: TransitionValues?
    ): Animator {
        val width = view.width
        val height = view.height
        val cx = (width * centerX).toInt()
        val cy = (height * centerY).toInt()
        val distance: Int = if (width > height) {
            max(width - cx, cx)
        } else {
            max(height - cy, cy)
        }

        val fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f)

        val slideAnim = ObjectAnimator.ofFloat(
            view,
            "y",
            tipViewBottomY - height,
            sliderViewY
        )
        val revealAnim = ViewAnimationUtils.createCircularReveal(view, cx, cy, distance.toFloat(), 0f)
        slideAnim.duration = revealAnim.duration
        fadeAnim.duration = revealAnim.duration

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(fadeAnim, slideAnim, revealAnim)
        return NoPauseAnimator(animatorSet)
    }


    private class NoPauseAnimator(private val mAnimator: Animator) : Animator() {
        private val mListeners: ArrayMap<AnimatorListener, AnimatorListener> =
            ArrayMap<AnimatorListener, AnimatorListener>()

        override fun addListener(listener: AnimatorListener) {
            val wrapper: AnimatorListener = AnimatorListenerWrapper(
                this,
                listener
            )
            if (!mListeners.containsKey(listener)) {
                mListeners[listener] = wrapper
                mAnimator.addListener(wrapper)
            }
        }

        override fun cancel() {
            mAnimator.cancel()
        }

        override fun end() {
            mAnimator.end()
        }

        override fun getDuration(): Long {
            return mAnimator.duration
        }

        override fun getInterpolator(): TimeInterpolator {
            return mAnimator.interpolator
        }

        override fun getListeners(): ArrayList<AnimatorListener> {
            return ArrayList<AnimatorListener>(mListeners.keys)
        }

        override fun getStartDelay(): Long {
            return mAnimator.startDelay
        }

        override fun isPaused(): Boolean {
            return mAnimator.isPaused
        }

        override fun isRunning(): Boolean {
            return mAnimator.isRunning
        }

        override fun isStarted(): Boolean {
            return mAnimator.isStarted
        }

        override fun removeAllListeners() {
            super.removeAllListeners()
            mListeners.clear()
            mAnimator.removeAllListeners()
        }

        override fun removeListener(listener: AnimatorListener) {
            val wrapper: AnimatorListener? = mListeners.get(listener)
            if (wrapper != null) {
                mListeners.remove(listener)
                mAnimator.removeListener(wrapper)
            }
        }

        override fun setDuration(durationMS: Long): Animator {
            mAnimator.duration = durationMS
            return this
        }

        override fun setInterpolator(timeInterpolator: TimeInterpolator) {
            mAnimator.interpolator = timeInterpolator
        }

        override fun setStartDelay(delayMS: Long) {
            mAnimator.startDelay = delayMS
        }

        override fun setTarget(target: Any?) {
            mAnimator.setTarget(target)
        }

        override fun setupEndValues() {
            mAnimator.setupEndValues()
        }

        override fun setupStartValues() {
            mAnimator.setupStartValues()
        }

        override fun start() {
            mAnimator.start()
        }
    }

    private class AnimatorListenerWrapper(
        private val mAnimator: Animator,
        private val mListener: Animator.AnimatorListener
    ) : Animator.AnimatorListener {


        override fun onAnimationStart(animation: Animator, isReverse: Boolean) {
            super.onAnimationStart(animation, isReverse)
        }

        override fun onAnimationEnd(animation: Animator, isReverse: Boolean) {
            super.onAnimationEnd(animation, isReverse)
        }

        override fun onAnimationStart(animator: Animator) {
            mListener.onAnimationStart(mAnimator)
        }

        override fun onAnimationEnd(animator: Animator) {
            mListener.onAnimationEnd(mAnimator)
        }

        override fun onAnimationCancel(animator: Animator) {
            mListener.onAnimationCancel(mAnimator)
        }

        override fun onAnimationRepeat(animator: Animator) {
            mListener.onAnimationRepeat(mAnimator)
        }
    }

    override fun updateLocation(sliderViewY: Float, tipViewBottomY: Float) {
        this.tipViewBottomY = tipViewBottomY
        this.sliderViewY = sliderViewY
    }
}