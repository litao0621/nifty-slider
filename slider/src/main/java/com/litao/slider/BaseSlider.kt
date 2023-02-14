package com.litao.slider

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.withStyledAttributes
import kotlin.math.max

/**
 * @author : litao
 * @date   : 2023/2/13 16:21
 */
abstract class BaseSlider constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr) {

    var trackPaint: Paint
    var trackSecondaryPaint: Paint
    var inactiveTrackPaint: Paint


    private lateinit var trackColor: ColorStateList
    private lateinit var trackSecondaryColor: ColorStateList
    private lateinit var trackColorInactive: ColorStateList

    private val trackRectF = RectF()

    var valueFrom = 0f
    var valueTo = 0f
    var value = 0f

    var viewHeight = 0

    var trackHeight = 0
        set(@IntRange(from = 0) value) {
            if (value != field){
                field = value
                invalidateTrack()
                updateViewLayout()
            }
        }

    var trackWidth = 0

    companion object{
        private const val HIGH_QUALITY_FLAGS = Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG
    }


    init {
        inactiveTrackPaint = Paint(HIGH_QUALITY_FLAGS).apply {
            style = Paint.Style.FILL
        }

        trackPaint = Paint(HIGH_QUALITY_FLAGS).apply {
            style = Paint.Style.FILL
        }

        trackSecondaryPaint = Paint(HIGH_QUALITY_FLAGS).apply {
            style = Paint.Style.FILL
        }

        processAttributes(context,attrs,defStyleAttr)

    }

    private fun processAttributes(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int) {
        context.withStyledAttributes(attrs, R.styleable.NiftySlider, defStyleAttr, R.style.NiftySlider) {
            valueFrom = getFloat(R.styleable.NiftySlider_android_valueFrom, 0.0f)
            valueTo = getFloat(R.styleable.NiftySlider_android_valueTo, 1.0f)
            value = getFloat(R.styleable.NiftySlider_android_value, 0.0f)

            trackHeight = getDimensionPixelOffset(R.styleable.NiftySlider_trackHeight,0)

            val trackColorList = getColorStateList(R.styleable.NiftySlider_trackColor)
            setTrackTintList(trackColorList?:AppCompatResources.getColorStateList(context,R.color.default_track_color))
            val trackSecondaryColor = getColorStateList(R.styleable.NiftySlider_trackSecondaryColor)
            setTrackSecondaryTintList(trackSecondaryColor?:AppCompatResources.getColorStateList(context,R.color.default_track_color))
            val trackInactiveColorList = getColorStateList(R.styleable.NiftySlider_trackColorInactive)
            setTrackInactiveTintList(trackInactiveColorList?:AppCompatResources.getColorStateList(context,R.color.default_track_inactive_color))

        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(
            widthMeasureSpec,
            MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        updateTrackWidth(w)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val yCenter = measuredHeight/2f
        val width = measuredWidth
        drawInactiveTrack(canvas,width,yCenter)
        if (value > valueFrom){
            drawTrack(canvas,width,yCenter)
        }


    }

    fun drawTrack(canvas: Canvas, width: Int, yCenter: Float) {
        trackRectF.set(
            0f + paddingLeft,
            yCenter - trackHeight/2f,
            paddingLeft + trackWidth * percentValue(value),
            yCenter + trackHeight/2f
        )
        canvas.drawRoundRect(
            trackRectF,
            trackHeight/2f,
            trackHeight/2f,
            trackPaint
        )
    }

    fun drawInactiveTrack(canvas: Canvas, width: Int, yCenter: Float) {
        trackRectF.set(
            0f + paddingLeft,
            yCenter - trackHeight/2f,
            width.toFloat() - paddingRight,
            yCenter + trackHeight/2f
        )
        canvas.drawRoundRect(
            trackRectF,
            trackHeight/2f,
            trackHeight/2f,
            inactiveTrackPaint
        )
    }

    private fun percentValue(value:Float):Float{
        return (value - valueFrom)/(valueTo - valueFrom)
    }

    private fun invalidateTrack() {
//        inactiveTrackPaint.strokeWidth = trackHeight.toFloat()
//        trackPaint.strokeWidth = trackHeight.toFloat()
//        trackSecondaryPaint.strokeWidth = trackHeight.toFloat()
    }

    fun updateViewLayout(){
        updateTrackWidth(width)
        if (viewHeightChanged()){
            requestLayout()
        }
    }



    fun viewHeightChanged():Boolean{
        val topBottomPadding = paddingTop + paddingBottom
        val minHeightWithTrack = topBottomPadding + trackHeight

        if (minHeightWithTrack == viewHeight){
            return false
        }else{
            viewHeight = minHeightWithTrack
            return true
        }
    }

    fun updateTrackWidth(viewWidth:Int){
        trackWidth = max(viewWidth - paddingLeft - paddingRight, 0)
    }


    fun setTrackTintList(color: ColorStateList) {
        if (this::trackColor.isInitialized && color == trackColor) {
            return
        }
        trackColor = color
        trackPaint.color = getColorForState(trackColor)
        invalidate()
    }

    fun setTrackSecondaryTintList(color: ColorStateList) {
        if (this::trackSecondaryColor.isInitialized && color == trackSecondaryColor) {
            return
        }
        trackSecondaryColor = color
        trackSecondaryPaint.color = getColorForState(trackSecondaryColor)
        invalidate()
    }

    fun setTrackInactiveTintList(color: ColorStateList) {
        if (this::trackColorInactive.isInitialized && color == trackColorInactive) {
            return
        }
        trackColorInactive = color
        inactiveTrackPaint.color = getColorForState(trackColorInactive)
        invalidate()
    }


    @ColorInt
    fun getColorForState(colorStateList: ColorStateList): Int {
        return colorStateList.getColorForState(drawableState, colorStateList.defaultColor)
    }




}