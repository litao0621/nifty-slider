package com.litao.slider.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

/**
 * @author : litao
 * @date   : 2023/3/20 18:08
 */
class ArrowView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
    private val path: Path = Path()

    private var color = Color.BLACK

    init {
        mPaint.style = Paint.Style.FILL
        mPaint.color = Color.BLUE
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val width = measuredWidth.toFloat()
        val height = measuredHeight.toFloat()

        path.apply {
            reset()
            moveTo(0f,0f)
            lineTo(width,0f)
            lineTo(width/2f,height)
            close()
        }

        canvas?.drawPath(path,mPaint)
    }

    fun setArrowColor(color:Int){
        this.color = color
        mPaint.color = this.color
        invalidate()
    }


}