package com.litao.slider

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.Dimension
import kotlin.math.roundToInt

/**
 * @author : litao
 * @date   : 2023/3/17 17:56
 */
class SliderTipView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val TIP_VIEW_ID = R.id.nifty_slider_tip_view

    private var slider: BaseSlider? = null

    private var locationOnScreenX = 0
    private var locationOnScreenY = 0

    /**
     * > 0 offset up
     * < 0 offset down
     */
    var verticalOffset = 0

    var isAttached = false

    init {
        id = TIP_VIEW_ID + hashCode()
        visibility = INVISIBLE
        setSize(300, 300)
        setBackgroundColor(Color.WHITE)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
    }

    fun setSize(width: Int, height: Int) {
        layoutParams = LayoutParams(width, height)
    }

    fun attachTipView(view: BaseSlider) {
        val contentView = getContentView(view)
        this.slider = view
        contentView?.let {
            val tipView = it.findViewById<SliderTipView>(TIP_VIEW_ID)
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
            verticalOffset = -(slider?.thumbRadius ?: 0) - dpToPx(12)
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
            updateLocationOnScreen(slider)
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
    fun onLocationChanged(centerX: Float, centerY: Float) {
        if (isAttached) {
            x = locationOnScreenX + centerX - width / 2
            y = locationOnScreenY + centerY - height + verticalOffset
        }
    }


    private fun dpToPx(@Dimension(unit = Dimension.DP) dp: Int): Int {
        val r = Resources.getSystem()
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics).roundToInt()
    }
}