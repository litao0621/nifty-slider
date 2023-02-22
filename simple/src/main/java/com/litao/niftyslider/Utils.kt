package com.litao.niftyslider

import android.content.Context
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.annotation.Dimension

/**
 * @author : litao
 * @date   : 2023/2/21 16:41
 */
object Utils {

    fun setColorAlpha(@ColorInt color: Int, alpha: Float): Int {
        val origin = (0xff ) and 0xff
        return color and 0x00ffffff or ((alpha * origin).toInt() shl 24)
    }

    fun dpToPx(context: Context, @Dimension(unit = Dimension.DP) dp: Int): Float {
        val r = context.resources
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics)
    }

}