package com.litao.slider.anim

import android.view.ViewGroup
import androidx.transition.Transition

/**
 * @author : litao
 * @date   : 2023/3/21 16:46
 */
fun interface TipViewAnimator {
    /**
     * create tip view custom  show/hide transitions
     */
    fun createTransition(): Transition?
}