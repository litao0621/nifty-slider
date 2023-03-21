package com.litao.slider.anim

import android.view.View
import android.view.ViewGroup

/**
 * @author : litao
 * @date   : 2023/3/21 16:46
 */
interface TipViewAnimator {
    /**
     * create custom show animation
     *
     * @return Whether to execute custom animations
     */
    fun executeShowAnim(view: ViewGroup): Boolean

    /**
     * create custom hide animation
     *
     * @return Whether to execute custom animations
     */
    fun executeHideAnim(view:ViewGroup): Boolean
}