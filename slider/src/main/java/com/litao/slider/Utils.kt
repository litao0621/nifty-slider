package com.litao.slider

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.Dimension
import kotlin.math.roundToInt

/**
 * @author : litao
 * @date   : 2023/3/20 17:37
 */
object Utils {
    fun dpToPx(@Dimension(unit = Dimension.DP) dp: Int): Int {
        val r = Resources.getSystem()
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics).roundToInt()
    }

    fun getWindowWidth(context: Context): Int {
        return dpToPx(context.resources.configuration.screenWidthDp)
    }
}