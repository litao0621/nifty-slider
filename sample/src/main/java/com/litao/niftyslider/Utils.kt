package com.litao.niftyslider

import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import kotlin.math.floor
import kotlin.math.roundToInt

/**
 * @author : litao
 * @date   : 2023/2/21 16:41
 */
object Utils {

    fun setColorAlpha(@ColorInt color: Int, alpha: Float): Int {
        val origin = (0xff ) and 0xff
        return color and 0x00ffffff or ((alpha * origin).toInt() shl 24)
    }

    fun toHexColorString(@ColorInt color: Int):String{
        return "#%06X".format(0xFFFFFF and color)
    }

    fun dpToPx(@Dimension(unit = Dimension.DP) dp: Int): Int {
        val r = Resources.getSystem()
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics).roundToInt()
    }

    fun dpToPxF(@Dimension(unit = Dimension.DP) dp: Float): Float {
        val r = Resources.getSystem()
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.displayMetrics)
    }


    fun formatVideoTime(milliseconds:Long):String{
        val totalSeconds = floor(milliseconds.toDouble() / 1000)
        val secondsLeft = totalSeconds % 3600
        val minutes = floor(secondsLeft / 60).toInt()
        val seconds = (secondsLeft % 60).toInt()
        val m = if (minutes < 10) {
             "0$minutes"
        }else{
            minutes.toString()
        }
        val s = if (seconds < 10) {
             "0$seconds";
        }else{
            seconds.toString()
        }
        return "$m:$s";
    }


}