package com.litao.slider

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

/**
 * @author : litao
 * @date   : 2023/3/17 17:56
 */
class SliderTipView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    val TIP_VIEW_ID = R.id.nifty_slider_tip_view


    init {
        id = TIP_VIEW_ID
        visibility = GONE
        setSize(300, 300)
        setBackgroundColor(Color.WHITE)
    }


    fun setSize(width:Int,height:Int){
        layoutParams = LayoutParams(width, height)
    }

    fun attachTipView(view: View) {
        val contentView = getContentView(view)
        contentView?.let {
            val tipView = it.findViewById<SliderTipView>(TIP_VIEW_ID)
            if (tipView == null) {
                it.addView(this)
            }
        }
    }

    fun detachTipView(view: View){
        val contentView = getContentView(view)
        contentView?.removeView(this)
    }


    fun getContentView(view: View?): ViewGroup? {
        if (view == null) {
            return null
        }
        val rootView = view.rootView
        val contentView = rootView.findViewById<ViewGroup>(android.R.id.content)
        if (contentView != null) {
            return contentView
        }
        return if (rootView !== view && rootView is ViewGroup) {
            rootView
        } else null
    }

    fun show(){
        visibility = VISIBLE
    }

    fun hide(){
        visibility = GONE
    }

    /**
     * Update tip view location
     */
    fun onLocationChanged(touchX:Int,value:Float,percentValue:Float){

    }
}