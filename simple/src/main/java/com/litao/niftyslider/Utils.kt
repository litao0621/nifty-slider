package com.litao.niftyslider

import androidx.annotation.ColorInt

/**
 * @author : litao
 * @date   : 2023/2/21 16:41
 */
object Utils {

    fun setColorAlpha(@ColorInt color: Int, alpha: Float): Int {
        val origin = (0xff ) and 0xff
        return color and 0x00ffffff or ((alpha * origin).toInt() shl 24)
    }

}