package com.litao.slider.model

import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt

/**
 * @author : litao
 * @date   : 2024/7/11 13:38
 */
data class IndicatorIcon(
    var icon:Drawable?,
    var size:Int,
    @ColorInt var tintColor:Int = -1
)

data class IndicatorText(
    var text:String,
    var fontSize:Float,
    @ColorInt var fontColor:Int = Color.BLACK
)