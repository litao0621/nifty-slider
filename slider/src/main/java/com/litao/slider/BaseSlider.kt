package com.litao.slider

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.IntRange
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.res.getColorStateListOrThrow
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.withTranslation
import androidx.core.math.MathUtils
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import kotlin.math.abs
import kotlin.math.max

/**
 * @author : litao
 * @date   : 2023/2/13 16:21
 */
abstract class BaseSlider constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {

    private var trackPaint: Paint
    private var trackSecondaryPaint: Paint
    private var inactiveTrackPaint: Paint


    private lateinit var trackColor: ColorStateList
    private lateinit var trackSecondaryColor: ColorStateList
    private lateinit var trackColorInactive: ColorStateList

    private val defaultThumbDrawable = MaterialShapeDrawable()
    private var thumbRadius = 0

    private val trackRectF = RectF()


    private var lastTouchEvent: MotionEvent? = null
    private var scaledTouchSlop = 0
    private var touchDownX = 0f
    private var isDragging = false

    private var valueFrom = 0f
    private var valueTo = 0f
    var value = 0f

    private var viewHeight = 0

    private var trackHeight = 0
        set(@IntRange(from = 0) value) {
            if (value != field) {
                field = value
                invalidateTrack()
                updateViewLayout()
            }
        }

    private var trackWidth = 0

    companion object {
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

        scaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop

        defaultThumbDrawable.shadowCompatibilityMode = MaterialShapeDrawable.SHADOW_COMPAT_MODE_ALWAYS

        processAttributes(context, attrs, defStyleAttr)

    }

    private fun processAttributes(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int) {
        context.withStyledAttributes(attrs, R.styleable.NiftySlider, defStyleAttr, R.style.NiftySlider) {
            valueFrom = getFloat(R.styleable.NiftySlider_android_valueFrom, 0.0f)
            valueTo = getFloat(R.styleable.NiftySlider_android_valueTo, 1.0f)
            value = getFloat(R.styleable.NiftySlider_android_value, 0.0f)

            trackHeight = getDimensionPixelOffset(R.styleable.NiftySlider_trackHeight, 0)

            val trackColorList = getColorStateList(R.styleable.NiftySlider_trackColor)
            setTrackTintList(
                trackColorList ?: AppCompatResources.getColorStateList(
                    context,
                    R.color.default_track_color
                )
            )
            val trackSecondaryColor = getColorStateList(R.styleable.NiftySlider_trackSecondaryColor)
            setTrackSecondaryTintList(
                trackSecondaryColor ?: AppCompatResources.getColorStateList(
                    context,
                    R.color.default_track_color
                )
            )
            val trackInactiveColorList = getColorStateList(R.styleable.NiftySlider_trackColorInactive)
            setTrackInactiveTintList(
                trackInactiveColorList ?: AppCompatResources.getColorStateList(
                    context,
                    R.color.default_track_inactive_color
                )
            )



            setThumbTintList(getColorStateListOrThrow(R.styleable.NiftySlider_thumbColor))
            setThumbRadius(getDimensionPixelOffset(R.styleable.NiftySlider_thumbRadius, 0))
            setThumbElevation(getDimension(R.styleable.NiftySlider_thumbElevation, 0f))
            setThumbShadowColor(getColor(R.styleable.NiftySlider_thumbShadowColor, 0))
            setThumbStrokeColor(getColorStateList(R.styleable.NiftySlider_thumbStrokeColor))
            setThumbStrokeWidth(getDimension(R.styleable.NiftySlider_thumbStrokeWidth, 0f))
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

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        trackPaint.color = getColorForState(trackColor)
        trackSecondaryPaint.color = getColorForState(trackSecondaryColor)
        inactiveTrackPaint.color = getColorForState(trackColorInactive)
        if (defaultThumbDrawable.isStateful) {
            defaultThumbDrawable.state = drawableState
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val yCenter = measuredHeight / 2f
        val width = measuredWidth
        drawInactiveTrack(canvas, width, yCenter)
        if (value > valueFrom) {
            drawTrack(canvas, width, yCenter)
        }

        drawThumb(canvas, trackWidth, yCenter)
    }

    fun drawTrack(canvas: Canvas, width: Int, yCenter: Float) {
        trackRectF.set(
            0f + paddingLeft,
            yCenter - trackHeight / 2f,
            paddingLeft + trackWidth * percentValue(value),
            yCenter + trackHeight / 2f
        )
        canvas.drawRoundRect(
            trackRectF,
            trackHeight / 2f,
            trackHeight / 2f,
            trackPaint
        )
    }

    fun drawInactiveTrack(canvas: Canvas, width: Int, yCenter: Float) {
        trackRectF.set(
            0f + paddingLeft,
            yCenter - trackHeight / 2f,
            width.toFloat() - paddingRight,
            yCenter + trackHeight / 2f
        )
        canvas.drawRoundRect(
            trackRectF,
            trackHeight / 2f,
            trackHeight / 2f,
            inactiveTrackPaint
        )
    }

    fun drawThumb(canvas: Canvas, width: Int, yCenter: Float) {
        canvas.withTranslation(
            (paddingLeft + (percentValue(value) * width).toInt()) - defaultThumbDrawable.bounds.width() / 2f,
            yCenter - (defaultThumbDrawable.bounds.height() / 2f)
        ) {
            defaultThumbDrawable.draw(canvas)
        }
    }

    private fun percentValue(value: Float): Float {
        return (value - valueFrom) / (valueTo - valueFrom)
    }

    private fun invalidateTrack() {
//        inactiveTrackPaint.strokeWidth = trackHeight.toFloat()
//        trackPaint.strokeWidth = trackHeight.toFloat()
//        trackSecondaryPaint.strokeWidth = trackHeight.toFloat()
    }

    fun updateViewLayout() {
        updateTrackWidth(width)
        if (viewHeightChanged()) {
            requestLayout()
        }
    }


    fun viewHeightChanged(): Boolean {
        val topBottomPadding = paddingTop + paddingBottom
        val minHeightWithTrack = topBottomPadding + trackHeight
        val minHeightWithThumb = topBottomPadding + thumbRadius * 2

        val tempHeight = max(minHeightWithTrack, minHeightWithThumb)

        if (tempHeight == viewHeight) {
            return false
        } else {
            viewHeight = tempHeight
            return true
        }
    }

    fun updateTrackWidth(viewWidth: Int) {
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

    fun setThumbRadius(@IntRange(from = 0) @Dimension radius: Int) {
        if (radius == thumbRadius) {
            return
        }
        thumbRadius = radius
        defaultThumbDrawable.shapeAppearanceModel =
            ShapeAppearanceModel.builder().setAllCorners(CornerFamily.ROUNDED, thumbRadius.toFloat()).build()
        defaultThumbDrawable.setBounds(0, 0, thumbRadius * 2, thumbRadius * 2)
        updateViewLayout()
    }

    fun setThumbTintList(thumbColor: ColorStateList) {
        if (thumbColor == defaultThumbDrawable.fillColor) {
            return
        }
        defaultThumbDrawable.fillColor = thumbColor
        invalidate()
    }

    fun setThumbElevation(elevation: Float) {
        defaultThumbDrawable.elevation = elevation
    }

    fun setThumbStrokeColor(thumbStrokeColor: ColorStateList?) {
        defaultThumbDrawable.strokeColor = thumbStrokeColor
        postInvalidate()
    }

    fun setThumbStrokeWidth(thumbStrokeWidth: Float) {
        defaultThumbDrawable.strokeWidth = thumbStrokeWidth
        postInvalidate()
    }

    fun setThumbShadowColor(@ColorInt shadowColor: Int) {
        defaultThumbDrawable.setShadowColor(shadowColor)
    }


    @ColorInt
    fun getColorForState(colorStateList: ColorStateList): Int {
        return colorStateList.getColorForState(drawableState, colorStateList.defaultColor)
    }

    private fun getValueByTouchPos(pos: Float): Float {
        return pos * (valueTo - valueFrom) + valueFrom
    }

    private fun getTouchPosByX(touchX: Float):Float{
        return MathUtils.clamp((touchX - paddingLeft) / trackWidth,0f,1f)
    }

    private fun isInVerticalScrollingContainer():Boolean{
        var p = parent
        while (p is ViewGroup) {
            val parent = p
            val canScrollVertically = parent.canScrollVertically(1) || parent.canScrollVertically(-1)
            if (canScrollVertically && parent.shouldDelayChildPressedState()) {
                return true
            }
            p = p.getParent()
        }
        return false
    }


    fun startDrag(event: MotionEvent){

    }

    fun stopDrag(event: MotionEvent){

    }

    fun trackTouchEvent(event: MotionEvent){
        val touchPos = getTouchPosByX(event.x)
        value = getValueByTouchPos(touchPos)
        invalidate()
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled){
            return false
        }

        val currentX = event.x

        when(event.action){
            MotionEvent.ACTION_DOWN -> {
                touchDownX = currentX

                if (isInVerticalScrollingContainer()){
                    //在纵向滑动布局中不处理down事件，优先外层滑动
                }else {
                    parent.requestDisallowInterceptTouchEvent(true)
                    requestFocus()
                    isDragging = true
                    startDrag(event)
                    trackTouchEvent(event)
                }
            }
            MotionEvent.ACTION_MOVE ->{
                if (!isDragging){
                    if (isInVerticalScrollingContainer() && abs(currentX-touchDownX) < scaledTouchSlop){
                        return false
                    }
                }
                parent.requestDisallowInterceptTouchEvent(true)
                isDragging = true
                trackTouchEvent(event)

            }
            MotionEvent.ACTION_UP -> {
                isDragging = false

                lastTouchEvent?.let {
                    if (it.action == MotionEvent.ACTION_DOWN && isClickTouch(it,event)){
                        trackTouchEvent(event)
                    }
                }

                stopDrag(event)
                invalidate()
            }
            MotionEvent.ACTION_CANCEL -> {
                isDragging = false

                stopDrag(event)
                invalidate()
            }
        }

        isPressed = isDragging
        lastTouchEvent = MotionEvent.obtain(event)
        return true
    }


    fun isClickTouch(startEvent:MotionEvent, endEvent:MotionEvent): Boolean {
        val differenceX = abs(startEvent.x - endEvent.x)
        val differenceY = abs(startEvent.y - endEvent.y)
        return !(differenceX > scaledTouchSlop || differenceY > scaledTouchSlop)
    }


}