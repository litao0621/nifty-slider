package com.litao.slider.widget

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import com.litao.slider.BaseSlider
import com.litao.slider.R
import com.litao.slider.Utils
import kotlin.math.roundToInt

/**
 * @author : litao
 * @date   : 2023/3/17 17:56
 */
class TipViewContainer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val TIP_VIEW_ID = R.id.nifty_slider_tip_view

    private var slider: BaseSlider? = null

    private val defaultSpace = Utils.dpToPx(8)

    private var locationOnScreenX = 0
    private var locationOnScreenY = 0

    private var customTipView:View? = null

    private var defaultTipView = DefaultTipView(context)

    var isTipTextAutoChange = true

    /**
     * > 0 offset up
     * < 0 offset down
     */
    var verticalOffset = 0

    var isAttached = false

    companion object{
        const val TAG = "TipViewContainer"
    }

    init {
        id = TIP_VIEW_ID + hashCode()
        visibility = INVISIBLE
        setSize(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
//        setBackgroundColor(Color.WHITE)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateLocation()
    }

    fun setSize(width: Int, height: Int) {
        layoutParams = LayoutParams(width, height)
    }

    fun attachTipView(view: BaseSlider) {
        val contentView = getContentView(view)
        this.slider = view
        contentView?.let {
            val tipView = it.findViewById<TipViewContainer>(TIP_VIEW_ID)
            if (tipView == null) {
                it.addView(this)
            }
            isAttached = true
        }

    }

    private fun updateLocationOnScreen(view: View?) {
        if (view != null) {
            val locationOnScreen = IntArray(2)
            view.getLocationOnScreen(locationOnScreen)
            locationOnScreenX = locationOnScreen[0]
            locationOnScreenY = locationOnScreen[1]
        }
    }

    private fun updateParams() {
        if (verticalOffset == 0) {
            verticalOffset = -(slider?.thumbRadius ?: 0) - defaultSpace
        }
    }

    fun detachTipView(view: View) {
        val contentView = getContentView(view)
        contentView?.removeView(this)
    }


    private fun getContentView(view: View?): ViewGroup? {
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

    fun show() {
        if (isAttached) {
            addTipViewIfNeed()
            updateLocationOnScreen(slider)
            updateLocation()
            updateParams()
            visibility = VISIBLE
        }
    }

    fun hide() {
        if (isAttached) {
            updateLocationOnScreen(slider)
            visibility = GONE
        }
    }

    /**
     * Update tip view location
     */
    fun onLocationChanged(cx: Float, cy: Float,value: Float) {
        if (isAttached) {
            updateLocation(cx,cy)
            if (isTipTextAutoChange) {
                setTipText(String.format("%.0f", value))
            }
        }
    }

    private fun updateLocation(cx: Float = getRelativeCX(), cy: Float = getRelativeCY()){
        x = locationOnScreenX + cx - width / 2
        y = locationOnScreenY + cy - height + verticalOffset
    }

    fun addTipViewIfNeed(){
        if (customTipView == null){
            if (childCount == 0){
                //add default tip view
                addView(defaultTipView, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
            }
        }else{
            // TODO: add custom tip view 
        }

    }

    fun setTipBackground(@ColorInt color:Int){
        defaultTipView.setTipBackground(color)
    }

    fun setTipTextColor(@ColorInt color:Int){
        defaultTipView.setTipTextColor(color)
    }

    fun setTipText(text:CharSequence){
        defaultTipView.setTipText(text)
    }

    private fun getRelativeCX(): Float {
        return slider?.getThumbCenterX() ?: 0f
    }

    private fun getRelativeCY(): Float {
        return slider?.getThumbCenterY() ?: 0f
    }
}