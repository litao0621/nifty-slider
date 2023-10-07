package com.litao.niftyslider.widget

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.litao.slider.NiftySlider
import kotlin.math.abs


/**
 * @author : litao
 * @date   : 2023/9/20 17:44
 */
class SliderTouchAreaView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val touchViewID = View.generateViewId()
    private var slider: NiftySlider? = null


    private var mInitialTouchX = 0f
    private var touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    private var isTouchMoved = false

    private var downEvent:MotionEvent? = null

    /**
     * 使用时请先调用bind方法来进行与slider进行绑定
     */
    fun bind(slider: NiftySlider) {
        this.slider = slider
    }


    override fun onFinishInflate() {
        super.onFinishInflate()
        val touchView = findViewById<View>(touchViewID)
        if (touchView == null) {
            //底部添加一个可响应事件的view来保证当前view能接收到完整到touch事件
            addViewInLayout(
                TextView(context).apply {
                    id = touchViewID
                    isClickable = true
                },
                0,
                LayoutParams(0, 0).apply {
                    leftToLeft = 0
                    topToTop = 0
                    rightToRight = 0
                    bottomToBottom = 0
                })
        }
    }


    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                isTouchMoved = false
                mInitialTouchX = ev.x
                downEvent = MotionEvent.obtain(ev)
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = abs(ev.x - mInitialTouchX)
                if (dx < touchSlop && !isTouchMoved) {
                    //do nothing
                }else{
                    isTouchMoved = true
                    downEvent?.let {
                        slider?.onTouchEvent(it)
                    }
                    downEvent = null
                    slider?.onTouchEvent(ev)
                }
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (isTouchMoved) {
                    slider?.onTouchEvent(ev)
                }
                downEvent = null
            }
        }
        return super.dispatchTouchEvent(ev)
    }

}