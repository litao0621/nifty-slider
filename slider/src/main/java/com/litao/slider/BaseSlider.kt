package com.litao.slider

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.IntRange
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.res.getColorStateListOrThrow
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.withTranslation
import androidx.core.math.MathUtils
import com.google.android.material.drawable.DrawableUtils
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import java.lang.reflect.InvocationTargetException
import kotlin.math.abs
import kotlin.math.ceil
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
    private lateinit var haloColor: ColorStateList

    private val defaultThumbDrawable = MaterialShapeDrawable()
    private var thumbRadius = 0
    private var thumbElevation = 0f
    private var isThumbWithinTrackBounds = false
    private var enableDrawHalo = true
    private var haloDrawable: RippleDrawable? = null
    private var haloRadius = 0

    private val trackRectF = RectF()
    private var thumbOffset = 0

    private var trackInnerHPadding = 0
    private var trackInnerVPadding = 0




    private var lastTouchEvent: MotionEvent? = null
    private var scaledTouchSlop = 0
    private var touchDownX = 0f
    private var isDragging = false
    private var isTackingStart = false

    var valueFrom = 0f
    var valueTo = 0f
    var value = 0f
        set(value) {
            if (value != field) {
                field = value
                invalidate()
            }
        }

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


    abstract fun onStartTacking()
    abstract fun onStopTacking()

    abstract fun onValueChanged(value: Float,fromUser:Boolean)


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
        context.withStyledAttributes(attrs, R.styleable.NiftySlider, defStyleAttr, R.style.Widget_NiftySlider) {
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
            setThumbWithinTrackBounds(getBoolean(R.styleable.NiftySlider_thumbWithinTrackBounds,false))
            setThumbElevation(getDimension(R.styleable.NiftySlider_thumbElevation, 0f))
            setThumbShadowColor(getColor(R.styleable.NiftySlider_thumbShadowColor, 0))
            setThumbStrokeColor(getColorStateList(R.styleable.NiftySlider_thumbStrokeColor))
            setThumbStrokeWidth(getDimension(R.styleable.NiftySlider_thumbStrokeWidth, 0f))

            setTrackInnerHPadding(getDimensionPixelOffset(R.styleable.NiftySlider_trackInnerHPadding,-1))
            setTrackInnerVPadding(getDimensionPixelOffset(R.styleable.NiftySlider_trackInnerVPadding,-1))
            setEnableDrawHalo(getBoolean(R.styleable.NiftySlider_enableDrawHalo,true))
            setHaloTintList(getColorStateListOrThrow(R.styleable.NiftySlider_haloColor))
            setHaloRadius(getDimensionPixelOffset(R.styleable.NiftySlider_haloRadius,0))

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
            0f + paddingLeft + trackInnerHPadding,
            yCenter - trackHeight / 2f,
            paddingLeft + trackInnerHPadding + thumbOffset*2 + (trackWidth - thumbOffset*2) * percentValue(value),
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
            0f + paddingLeft + trackInnerHPadding,
            yCenter - trackHeight / 2f,
            width.toFloat() - paddingRight - trackInnerHPadding,
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
            (paddingLeft + trackInnerHPadding + thumbOffset + (percentValue(value) * (width - thumbOffset*2)).toInt()) - defaultThumbDrawable.bounds.width() / 2f,
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
        }else{
            invalidate()
        }
    }


    fun viewHeightChanged(): Boolean {
        val topBottomPadding = paddingTop + paddingBottom
        val minHeightWithTrack = topBottomPadding + trackHeight
        val minHeightWithThumb = topBottomPadding + thumbRadius * 2 + trackInnerVPadding * 2

        val tempHeight = max(minHeightWithTrack, minHeightWithThumb)

        if (tempHeight == viewHeight) {
            return false
        } else {
            viewHeight = tempHeight
            return true
        }
    }

    fun updateTrackWidth(viewWidth: Int) {
        trackWidth = max(viewWidth - paddingLeft - paddingRight - trackInnerHPadding*2, 0)
    }

    /**
     * Sets the horizontal inner padding of the track.
     * 主要处理thumb超出部分的视图，使thumb展示正常
     * 也可以使用 [BaseSlider.setThumbWithinTrackBounds] 来将thumb直接控制在track内部
     *
     * @param padding track左右的padding值，
     */
    fun setTrackInnerHPadding(padding: Int = -1) {
        val innerHPadding = if (padding == -1) {
            if (isThumbWithinTrackBounds){
                //thumb with in track bounds 模式下只需要要考虑超出阴影视图
                ceil(thumbElevation).toInt()
            }else{
                thumbRadius + ceil(thumbElevation).toInt()
            }

        } else {
            padding
        }

        if (innerHPadding == trackInnerHPadding){
            return
        }

        trackInnerHPadding = innerHPadding
        updateViewLayout()
    }

    /**
     * Sets the vertical inner padding of the track.
     * 主要处理thumb阴影超出部分的视图，使thumb展示正常
     *
     * @param padding track左右的padding值，
     */
    fun setTrackInnerVPadding(padding:Int){
        val innerVPadding = if (padding == -1){
            ceil(thumbElevation).toInt()
        }else{
            padding
        }

        if (innerVPadding == trackInnerVPadding){
            return
        }

        trackInnerVPadding = innerVPadding
        updateViewLayout()
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
        defaultThumbDrawable.setBounds(
            0,
            0,
            thumbRadius * 2,
            thumbRadius * 2
        )
        updateViewLayout()
    }

    /**
     * Sets whether the thumb within track bounds
     * 正常模式下滑块thumb是以track的起始位置为中心,thumb较大时左半部分会超出视图边界
     * 某些样式下，thumb需要控制在track的范围以内，可通过此方法来启用此项功能
     *
     * @param isInBounds thumb 是否需要绘制在 track 范围以内
     */
    fun setThumbWithinTrackBounds(isInBounds: Boolean){

        isThumbWithinTrackBounds = isInBounds

        val offset = if (isInBounds) {
            //启用状态下直接使用thumb的半径做为向内偏移的具体数值
            thumbRadius
        } else {
            0
        }

        if (thumbOffset == offset) {
            return
        }
        thumbOffset = offset
        setTrackInnerHPadding()
        updateViewLayout()
    }

    fun setThumbTintList(thumbColor: ColorStateList) {
        if (thumbColor == defaultThumbDrawable.fillColor) {
            return
        }
        defaultThumbDrawable.fillColor = thumbColor
        invalidate()
    }

    fun setHaloTintList(haloColor: ColorStateList){
        if (this::haloColor.isInitialized && this.haloColor == haloColor){
            return
        }

        this.haloColor = haloColor
        if (!shouldDrawCompatHalo() && background is RippleDrawable){
            (background as RippleDrawable).setColor(haloColor)
            return
        }

        invalidate()

    }

    fun setHaloRadius(@IntRange(from = 0) @Dimension radius:Int){
        if (haloRadius == radius){
            return
        }

        haloRadius = radius
        if (!shouldDrawCompatHalo() && enableDrawHalo && background is RippleDrawable){
            hookRippleRadius(background as RippleDrawable,haloRadius)
            return
        }
        postInvalidate()
    }

    fun setThumbElevation(elevation: Float) {
        defaultThumbDrawable.elevation = elevation

        thumbElevation = elevation
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

    fun setEnableDrawHalo(enable: Boolean) {
        enableDrawHalo = enable
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && haloDrawable == null && enable) {
            background = ContextCompat.getDrawable(context, R.drawable.halo_background)
            haloDrawable = background as RippleDrawable
        }
    }

    private fun updateHaloHotspot(){
        if (enableDrawHalo) {
            if (!shouldDrawCompatHalo() && measuredWidth > 0) {
                if (background is RippleDrawable) {
                    val haloX =
                        (paddingLeft + trackInnerHPadding + thumbOffset + (percentValue(value) * (trackWidth - thumbOffset * 2)).toInt())
                    val haloY = viewHeight / 2
                    DrawableCompat.setHotspotBounds(
                        background,
                        haloX - haloRadius,
                        haloY - haloRadius,
                        haloX + haloRadius,
                        haloY + haloRadius
                    )
                }
            }
        }
    }

    fun shouldDrawCompatHalo():Boolean{
       return Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP || background !is RippleDrawable
    }


    @ColorInt
    fun getColorForState(colorStateList: ColorStateList): Int {
        return colorStateList.getColorForState(drawableState, colorStateList.defaultColor)
    }

    private fun getValueByTouchPos(pos: Float): Float {
        return pos * (valueTo - valueFrom) + valueFrom
    }

    private fun getTouchPosByX(touchX: Float):Float{
        return MathUtils.clamp((touchX - paddingLeft - trackInnerHPadding) / trackWidth,0f,1f)
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


    private fun startTacking(event: MotionEvent){
        isTackingStart = true
        onStartTacking()
    }

    private fun stopTacking(event: MotionEvent){
        if (isTackingStart) {
            onStopTacking()
        }
        isTackingStart = false
        invalidate()
    }



    private fun trackTouchEvent(event: MotionEvent){
        val touchPos = getTouchPosByX(event.x)
        value = getValueByTouchPos(touchPos)
        onValueChanged(value,true)
        updateHaloHotspot()
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
                    startTacking(event)
                    trackTouchEvent(event)
                }
            }
            MotionEvent.ACTION_MOVE ->{
                if (!isDragging){
                    if (isInVerticalScrollingContainer() && abs(currentX-touchDownX) < scaledTouchSlop){
                        return false
                    }
                    parent.requestDisallowInterceptTouchEvent(true)
                    startTacking(event)
                }

                isDragging = true
                trackTouchEvent(event)

            }
            MotionEvent.ACTION_UP -> {
                isDragging = false

                lastTouchEvent?.let {
                    if (it.action == MotionEvent.ACTION_DOWN && isClickTouch(it,event)){
                        startTacking(event)
                        trackTouchEvent(event)
                    }
                }

                stopTacking(event)

            }
            MotionEvent.ACTION_CANCEL -> {
                isDragging = false
                stopTacking(event)
            }
        }

        isPressed = isDragging
        lastTouchEvent = MotionEvent.obtain(event)
        return true
    }


    private fun isClickTouch(startEvent:MotionEvent, endEvent:MotionEvent): Boolean {
        val differenceX = abs(startEvent.x - endEvent.x)
        val differenceY = abs(startEvent.y - endEvent.y)
        return !(differenceX > scaledTouchSlop || differenceY > scaledTouchSlop)
    }

    private fun hookRippleRadius(drawable:RippleDrawable,radius:Int){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            drawable.radius = radius
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                val setMaxRadiusMethod =
                    RippleDrawable::class.java.getDeclaredMethod("setMaxRadius", Int::class.javaPrimitiveType)
                setMaxRadiusMethod.invoke(drawable, radius)
            } catch (e: NoSuchMethodException) {
                throw IllegalStateException("Couldn't set RippleDrawable radius", e)
            } catch (e: InvocationTargetException) {
                throw IllegalStateException("Couldn't set RippleDrawable radius", e)
            } catch (e: IllegalAccessException) {
                throw IllegalStateException("Couldn't set RippleDrawable radius", e)
            }
        }
    }


}