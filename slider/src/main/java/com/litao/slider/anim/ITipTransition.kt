package com.litao.slider.anim

/**
 * @author : litao
 * @date   : 2023/3/23 11:13
 */
fun interface ITipTransition {
    fun updateLocation(sliderViewY: Float, tipViewBottomY: Float)
}