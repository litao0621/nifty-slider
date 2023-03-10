package com.litao.slider

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import androidx.annotation.*
import androidx.annotation.IntRange
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.res.getColorStateListOrThrow
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.withTranslation
import androidx.core.math.MathUtils
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import java.lang.reflect.InvocationTargetException
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.roundToInt

/**
 * @author : litao
 * @date   : 2023/2/13 16:21
 */
abstract class BaseSlider constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {

    private var trackPaint: Paint
    private var trackSecondaryPaint: Paint
    private var ticksPaint: Paint
    private var inactiveTicksPaint: Paint
    private var inactiveTrackPaint: Paint
    private var thumbTextPaint: Paint
    private var haloPaint: Paint
    private var debugPaint: Paint


    private lateinit var trackColor: ColorStateList
    private lateinit var trackSecondaryColor: ColorStateList
    private lateinit var trackColorInactive: ColorStateList
    private lateinit var ticksColor: ColorStateList
    private lateinit var ticksColorInactive: ColorStateList
    private lateinit var thumbTextColor: ColorStateList
    private lateinit var haloColor: ColorStateList

    private val defaultThumbDrawable = MaterialShapeDrawable()
    private var customThumbDrawable: Drawable? = null

    private var thumbWidth = -1
    private var thumbHeight = -1
    private var thumbVOffset = 0
    private var thumbElevation = 0f
    private var isThumbWithinTrackBounds = false
    private var thumbText: String? = null

    private var enableDrawHalo = true
    private var haloDrawable: RippleDrawable? = null
    private var haloRadius = 0
    private var tickRadius = 0f


    private val trackRectF = RectF()
    private var thumbOffset = 0

    private var trackInnerHPadding = 0
    private var trackInnerVPadding = 0
    private var trackCornerRadius = -1


    private var lastTouchEvent: MotionEvent? = null
    private var scaledTouchSlop = 0
    private var touchDownX = 0f
    private var isDragging = false
    private var isTackingStart = false

    private var hasDirtyData = false

    var enableHapticFeedback = false

    var valueFrom = 0f
        set(value) {
            if (field != value) {
                field = value
                hasDirtyData = true
                postInvalidate()
            }
        }

    var valueTo = 0f
        set(value) {
            if (field != value) {
                field = value
                hasDirtyData = true
                postInvalidate()
            }
        }

    var value = 0f
        private set

    var stepSize = 0.0f
        set(value) {
            if (field != value && value > 0) {
                field = value
                hasDirtyData = true
                postInvalidate()
            }
        }

    var tickVisible = false

    //?????????????????????
    private var sourceViewHeight = 0

    //????????????????????????????????????thumb???thumb shadow???track????????????????????????
    private var viewHeight = 0

    var trackHeight = 0
        set(@IntRange(from = 0) value) {
            if (value != field) {
                field = value
                updateViewLayout()
            }
        }

    var trackWidth = 0

    companion object {
        var DEBUG_MODE = false

        private const val HIGH_QUALITY_FLAGS = Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG

        private const val HALO_ALPHA = 63
    }


    abstract fun onStartTacking()
    abstract fun onStopTacking()

    abstract fun onValueChanged(value: Float, fromUser: Boolean)

    abstract fun dispatchDrawInactiveTrackBefore(canvas: Canvas, trackRect: RectF, yCenter: Float): Boolean
    abstract fun drawInactiveTrackAfter(canvas: Canvas, trackRect: RectF, yCenter: Float)

    abstract fun dispatchDrawTrackBefore(canvas: Canvas, trackRect: RectF, yCenter: Float): Boolean
    abstract fun drawTrackAfter(canvas: Canvas, trackRect: RectF, yCenter: Float)


    abstract fun dispatchDrawThumbBefore(canvas: Canvas, cx: Float, cy: Float): Boolean
    abstract fun drawThumbAfter(canvas: Canvas, cx: Float, cy: Float)


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

        ticksPaint = Paint(HIGH_QUALITY_FLAGS).apply {
            style = Paint.Style.FILL
        }

        inactiveTicksPaint = Paint(HIGH_QUALITY_FLAGS).apply {
            style = Paint.Style.FILL
        }

        thumbTextPaint = Paint(HIGH_QUALITY_FLAGS).apply {
            style = Paint.Style.FILL
            textAlign = Paint.Align.CENTER
        }

        haloPaint = Paint(HIGH_QUALITY_FLAGS).apply {
            style = Paint.Style.FILL
        }

        debugPaint = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = 1f
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
            stepSize = getFloat(R.styleable.NiftySlider_android_stepSize, 0.0f)

            tickVisible = getBoolean(R.styleable.NiftySlider_ticksVisible, false)
            enableHapticFeedback = getBoolean(R.styleable.NiftySlider_android_hapticFeedbackEnabled, false)

            sourceViewHeight = getLayoutDimension(R.styleable.NiftySlider_android_layout_height, 0)
            trackHeight = getDimensionPixelOffset(R.styleable.NiftySlider_trackHeight, 0)

            setTrackTintList(
                getColorStateList(R.styleable.NiftySlider_trackColor) ?: AppCompatResources.getColorStateList(
                    context,
                    R.color.default_track_color
                )
            )
            setTrackSecondaryTintList(
                getColorStateList(R.styleable.NiftySlider_trackSecondaryColor) ?: AppCompatResources.getColorStateList(
                    context,
                    R.color.default_track_color
                )
            )
            setTrackInactiveTintList(
                getColorStateList(R.styleable.NiftySlider_trackColorInactive) ?: AppCompatResources.getColorStateList(
                    context,
                    R.color.default_track_inactive_color
                )
            )

            setTicksTintList(
                getColorStateList(R.styleable.NiftySlider_ticksColor) ?: AppCompatResources.getColorStateList(
                    context,
                    R.color.default_ticks_color
                )
            )
            setTicksInactiveTintList(
                getColorStateList(R.styleable.NiftySlider_ticksColorInactive) ?: AppCompatResources.getColorStateList(
                    context,
                    R.color.default_ticks_inactive_color
                )
            )


            val thumbW = getDimensionPixelOffset(R.styleable.NiftySlider_thumbWidth, -1)
            val thumbH = getDimensionPixelOffset(R.styleable.NiftySlider_thumbHeight, -1)

            setThumbTintList(getColorStateListOrThrow(R.styleable.NiftySlider_thumbColor))
            thumbRadius = getDimensionPixelOffset(R.styleable.NiftySlider_thumbRadius, 0)
            setThumbWidthAndHeight(thumbW, thumbH)
            setThumbVOffset(getDimensionPixelOffset(R.styleable.NiftySlider_thumbVOffset, 0))
            setThumbWithinTrackBounds(getBoolean(R.styleable.NiftySlider_thumbWithinTrackBounds, false))
            setThumbElevation(getDimension(R.styleable.NiftySlider_thumbElevation, 0f))
            setThumbShadowColor(getColor(R.styleable.NiftySlider_thumbShadowColor, 0))
            setThumbStrokeColor(getColorStateList(R.styleable.NiftySlider_thumbStrokeColor))
            setThumbStrokeWidth(getDimension(R.styleable.NiftySlider_thumbStrokeWidth, 0f))
            setThumbText(getString(R.styleable.NiftySlider_thumbText) ?: "")
            setThumbTextTintList(
                getColorStateList(R.styleable.NiftySlider_thumbTextColor) ?: ColorStateList.valueOf(
                    Color.WHITE
                )
            )
            setThumbTextSize(getDimension(R.styleable.NiftySlider_thumbTextSize, 10f))
            setThumbTextBold(getBoolean(R.styleable.NiftySlider_thumbTextBold, false))

            setTrackInnerHPadding(getDimensionPixelOffset(R.styleable.NiftySlider_trackInnerHPadding, -1))
            setTrackInnerVPadding(getDimensionPixelOffset(R.styleable.NiftySlider_trackInnerVPadding, -1))
            setTrackCornersRadius(getDimensionPixelOffset(R.styleable.NiftySlider_trackCornersRadius, -1))
            setEnableDrawHalo(getBoolean(R.styleable.NiftySlider_enableDrawHalo, true))
            setHaloTintList(getColorStateListOrThrow(R.styleable.NiftySlider_haloColor))
            setHaloRadius(getDimensionPixelOffset(R.styleable.NiftySlider_haloRadius, 0))

            setTickRadius(getDimension(R.styleable.NiftySlider_tickRadius, 0.0f))

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
        updateHaloHotspot()
    }

    override fun drawableStateChanged() {
        super.drawableStateChanged()
        trackPaint.color = getColorForState(trackColor)
        trackSecondaryPaint.color = getColorForState(trackSecondaryColor)
        ticksPaint.color = getColorForState(ticksColor)
        inactiveTicksPaint.color = getColorForState(ticksColorInactive)
        inactiveTrackPaint.color = getColorForState(trackColorInactive)
        if (defaultThumbDrawable.isStateful) {
            defaultThumbDrawable.state = drawableState
        }
        thumbTextPaint.color = getColorForState(thumbTextColor)
        haloPaint.color = getColorForState(haloColor)
        haloPaint.alpha = HALO_ALPHA
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (hasDirtyData) {
            validateDirtyData()
        }

        drawDebugArea(canvas)

        val yCenter = measuredHeight / 2f
        val width = measuredWidth
        drawInactiveTrack(canvas, width, yCenter)
        drawTrack(canvas, width, yCenter)


        drawTicks(canvas, trackWidth, yCenter)

        if ((isDragging || isFocused) && isEnabled) {
            //??????v23???????????????????????????
            drawCompatHaloIfNeed(canvas, trackWidth, yCenter)
        }

        drawThumb(canvas, trackWidth, yCenter)
    }

    override fun invalidateDrawable(drawable: Drawable) {
        super.invalidateDrawable(drawable)
        invalidate()
    }

    private fun drawDebugArea(canvas: Canvas) {
        val offset = 1
        if (DEBUG_MODE) {
            debugPaint.color = Color.RED
            canvas.drawRect(
                0f + offset,
                0f + offset,
                canvas.width.toFloat() - offset,
                canvas.height.toFloat() - offset,
                debugPaint
            )
            debugPaint.color = Color.BLUE
            canvas.drawLine(
                0f,
                canvas.height / 2f,
                canvas.width.toFloat(),
                canvas.height / 2f,
                debugPaint
            )
            canvas.drawLine(
                canvas.width / 2f,
                0f,
                canvas.width / 2f,
                canvas.height.toFloat(),
                debugPaint
            )

        }
    }

    /**
     * draw active track
     * ??????????????????????????????????????????????????????[trackColorInactive]??????????????????????????????????????????????????????????????????
     */
    private fun drawTrack(canvas: Canvas, width: Int, yCenter: Float) {
        trackRectF.set(
            0f + paddingLeft + trackInnerHPadding,
            yCenter - trackHeight / 2f,
            paddingLeft + trackInnerHPadding + thumbOffset * 2 + (trackWidth - thumbOffset * 2) * percentValue(value),
            yCenter + trackHeight / 2f
        )

        if (!dispatchDrawTrackBefore(canvas, trackRectF, yCenter)) {

            val cornerRadius = if (trackCornerRadius == -1) trackHeight / 2f else trackCornerRadius.toFloat()

            if (value > valueFrom) {
                canvas.drawRoundRect(
                    trackRectF,
                    cornerRadius,
                    cornerRadius,
                    trackPaint
                )
            }
        }

        drawTrackAfter(canvas, trackRectF, yCenter)
    }

    /**
     * draw inactive track
     */
    private fun drawInactiveTrack(canvas: Canvas, width: Int, yCenter: Float) {
        trackRectF.set(
            0f + paddingLeft + trackInnerHPadding,
            yCenter - trackHeight / 2f,
            width.toFloat() - paddingRight - trackInnerHPadding,
            yCenter + trackHeight / 2f
        )

        if (!dispatchDrawInactiveTrackBefore(canvas, trackRectF, yCenter)) {

            val cornerRadius = if (trackCornerRadius == -1) trackHeight / 2f else trackCornerRadius.toFloat()

            canvas.drawRoundRect(
                trackRectF,
                cornerRadius,
                cornerRadius,
                inactiveTrackPaint
            )
        }

        drawInactiveTrackAfter(canvas, trackRectF, yCenter)
    }

    /**
     * Draw thumb
     * ???[setThumbWithinTrackBounds]????????????thumb???????????????[thumbRadius]??????
     */
    private fun drawThumb(canvas: Canvas, width: Int, yCenter: Float) {

        val thumbDrawable = customThumbDrawable ?: defaultThumbDrawable

        val cx = paddingLeft + trackInnerHPadding + thumbOffset + (percentValue(value) * (width - thumbOffset * 2))
        val cy = yCenter - (thumbDrawable.bounds.height() / 2f) + thumbVOffset
        val tx = cx - thumbDrawable.bounds.width() / 2f
        if (!dispatchDrawThumbBefore(canvas, cx, yCenter)) {
            canvas.withTranslation(tx, cy) {
                thumbDrawable.draw(canvas)
            }

            //draw thumb text if needed
            thumbText?.let {
                val baseline = yCenter - (thumbTextPaint.fontMetricsInt.bottom + thumbTextPaint.fontMetricsInt.top) / 2
                canvas.drawText(
                    it,
                    cx,
                    baseline,
                    thumbTextPaint
                )
            }

        }

        drawThumbAfter(canvas, cx, yCenter)
    }

    /**
     * Draw compat halo
     * ???????????????????????????
     */
    private fun drawCompatHaloIfNeed(canvas: Canvas, width: Int, yCenter: Float) {
        if (shouldDrawCompatHalo() && enableDrawHalo) {
            val centerX =
                paddingLeft + trackInnerHPadding + thumbOffset + percentValue(value) * (width - thumbOffset * 2)

            //?????????????????????????????????
            if (parent is ViewGroup) {
                (parent as ViewGroup).clipChildren = false
            }

            canvas.drawCircle(centerX, yCenter, haloRadius.toFloat(), haloPaint)
        }
    }


    /**
     * draw tick
     */
    private fun drawTicks(canvas: Canvas, width: Int, yCenter: Float) {
        if (enableStepMode() && tickVisible) {
            val drawWidth = width - thumbOffset * 2 - tickRadius * 2
            val tickCount: Int = ((valueTo - valueFrom) / stepSize + 1).toInt()
            val stepWidth = drawWidth / (tickCount - 1).toFloat()
            val activeWidth = percentValue(value) * width + paddingLeft + trackInnerHPadding + thumbOffset

            for (i in 0 until tickCount) {
                val starLeft = paddingLeft + trackInnerHPadding + thumbOffset + tickRadius
                val cx = starLeft + i * stepWidth

                val circlePaint = if (cx <= activeWidth) {
                    ticksPaint
                } else {
                    inactiveTicksPaint
                }

                canvas.drawCircle(
                    starLeft + i * stepWidth,
                    yCenter,
                    tickRadius,
                    circlePaint
                )
            }
        }
    }

    /**
     * Returns a number between 0 and 1 with [BaseSlider.value]
     * ??????value??????????????????????????????0????????????1?????????
     */
    fun percentValue(v: Float = value): Float {
        return (v - valueFrom) / (valueTo - valueFrom)
    }


    /**
     * This method is called before the onDraw.make sure parameter is valid
     * ????????????????????????????????????????????????
     */
    private fun validateDirtyData() {
        if (hasDirtyData) {
            validateValueFrom()
            validateValueTo()
            validateValue()
            hasDirtyData = false
        }
    }

    /**
     * ??????[valueFrom]?????????
     */
    private fun validateValueFrom() {
        if (valueFrom > valueTo) {
            throw IllegalStateException("valueFrom($valueFrom) must be smaller than valueTo($valueTo)")
        }
    }

    /**
     * ??????[valueTo]?????????
     */
    private fun validateValueTo() {
        if (valueTo <= valueFrom) {
            throw IllegalStateException("valueTo($valueTo) must be greater than valueFrom($valueFrom)")
        }
    }

    /**
     * ??????[BaseSlider.value]??????????????????????????????????????????
     */
    private fun validateValue() {
        //value ???????????????????????????????????????
        if (value < valueFrom) {
            value = valueFrom
        } else if (value > valueTo) {
            value = valueTo
        }
    }

    fun updateViewLayout() {
        updateTrackWidth(width)
        if (viewHeightChanged()) {
            requestLayout()
        } else {
            invalidate()
        }
    }


    /**
     * Returns true if view height changed
     * ??????????????????????????????????????????????????????????????????
     */
    fun viewHeightChanged(): Boolean {
        val topBottomPadding = paddingTop + paddingBottom
        val minHeightWithTrack = topBottomPadding + trackHeight
        val minHeightWithThumb = topBottomPadding + thumbRadius * 2 + trackInnerVPadding * 2

        val tempHeight = max(minHeightWithTrack, minHeightWithThumb)

        if (tempHeight == viewHeight) {
            return false
        } else {
            viewHeight = max(tempHeight, sourceViewHeight)
            return true
        }
    }

    /**
     * update track real draw width
     * ????????????????????????????????????????????????????????????padding????????????????????????[trackInnerHPadding]??????
     */
    fun updateTrackWidth(viewWidth: Int) {
        trackWidth = max(viewWidth - paddingLeft - paddingRight - trackInnerHPadding * 2, 0)
    }

    /**
     * Sets the slider's [BaseSlider.value]
     * ????????????step size??? value ???????????????step size????????????
     *
     * @param value ?????????????????? [valueTo] ???????????? [valueFrom]
     */
    fun setValue(value: Float) {
        if (this.value != value) {
            this.value = value
            hasDirtyData = true
            onValueChanged(value, false)
            postInvalidate()
        }
    }

    /**
     * Sets the horizontal inner padding of the track.
     * ????????????thumb???????????????????????????thumb????????????
     * ??????????????? [BaseSlider.setThumbWithinTrackBounds] ??????thumb???????????????track??????
     *
     * @see R.attr.trackInnerHPadding
     *
     * @param padding track?????????padding??????
     */
    fun setTrackInnerHPadding(padding: Int = -1) {
        val innerHPadding = if (padding == -1) {
            if (isThumbWithinTrackBounds) {
                //thumb with in track bounds ?????????????????????????????????????????????
                ceil(thumbElevation).toInt()
            } else {
                thumbRadius + ceil(thumbElevation).toInt()
            }

        } else {
            padding
        }

        if (innerHPadding == trackInnerHPadding) {
            return
        }

        trackInnerHPadding = innerHPadding
        updateViewLayout()
    }

    /**
     * Sets the vertical inner padding of the track.
     * ????????????thumb?????????????????????????????????thumb????????????
     *
     * @see R.attr.trackInnerVPadding
     *
     * @param padding track?????????padding??????
     */
    fun setTrackInnerVPadding(padding: Int) {
        val innerVPadding = if (padding == -1) {
            ceil(thumbElevation).toInt()
        } else {
            padding
        }

        if (innerVPadding == trackInnerVPadding) {
            return
        }

        trackInnerVPadding = innerVPadding
        updateViewLayout()
    }


    /**
     * Sets the radius of the track corners.
     *
     * ???????????????????????????
     * @see R.attr.trackCornersRadius
     *
     * @param radius ????????????
     */
    fun setTrackCornersRadius(@IntRange(from = 0) @Dimension radius: Int) {
        if (radius == trackCornerRadius) {
            return
        }
        trackCornerRadius = radius
        postInvalidate()
    }

    /**
     * Sets the color for the track
     *
     * @see R.attr.trackColor
     */
    fun setTrackTintList(color: ColorStateList) {
        if (this::trackColor.isInitialized && color == trackColor) {
            return
        }
        trackColor = color
        trackPaint.color = getColorForState(trackColor)
        invalidate()
    }

    /**
     * Sets the color for the secondary track
     *
     * @see R.attr.trackSecondaryColor
     *
     * eg.?????????????????????????????????????????????,??????????????????????????????????????????
     */
    fun setTrackSecondaryTintList(color: ColorStateList) {
        if (this::trackSecondaryColor.isInitialized && color == trackSecondaryColor) {
            return
        }
        trackSecondaryColor = color
        trackSecondaryPaint.color = getColorForState(trackSecondaryColor)
        invalidate()
    }

    /**
     * Sets the inactive color for the track
     *
     * @see R.attr.trackColorInactive
     */
    fun setTrackInactiveTintList(color: ColorStateList) {
        if (this::trackColorInactive.isInitialized && color == trackColorInactive) {
            return
        }
        trackColorInactive = color
        inactiveTrackPaint.color = getColorForState(trackColorInactive)
        invalidate()
    }

    /**
     * Sets the color for the tick
     *
     * @see R.attr.ticksColor
     */
    fun setTicksTintList(color: ColorStateList) {
        if (this::ticksColor.isInitialized && color == ticksColor) {
            return
        }
        ticksColor = color
        ticksPaint.color = getColorForState(ticksColor)
        invalidate()
    }

    /**
     * Sets the inactive color for the tick
     *
     * @see R.attr.ticksColorInactive
     */
    fun setTicksInactiveTintList(color: ColorStateList) {
        if (this::ticksColorInactive.isInitialized && color == ticksColorInactive) {
            return
        }
        ticksColorInactive = color
        inactiveTicksPaint.color = getColorForState(ticksColorInactive)
        invalidate()
    }

    /**
     * Sets the radius of the tick in pixels.
     * ????????????????????????
     *
     * @see R.attr.tickRadius
     */
    fun setTickRadius(@FloatRange(from = 0.0) @Dimension tickRadius: Float) {
        if (this.tickRadius != tickRadius) {
            this.tickRadius = tickRadius
            postInvalidate()
        }
    }

    /**
     * Sets the text of the thumb
     *
     * @see R.attr.thumbText
     */
    fun setThumbText(text: String?) {
        if (this.thumbText != text) {
            this.thumbText = text
            postInvalidate()
        }
    }

    /**
     * Sets the radius of the thumb in pixels.
     * ????????????????????????
     * ?????????????????????drawable??????????????????
     *
     * @see R.attr.thumbRadius
     *
     * @param radius ????????????
     */
    var thumbRadius = 0
        set(@IntRange(from = 0) @Dimension value) {
            if (field == value) {
                return
            }
            field = value
            defaultThumbDrawable.shapeAppearanceModel =
                ShapeAppearanceModel.builder().setAllCorners(CornerFamily.ROUNDED, value.toFloat()).build()
            defaultThumbDrawable.setBounds(
                0,
                0,
                value * 2,
                value * 2
            )

            customThumbDrawable?.let {
                adjustCustomThumbDrawableBounds(it)
            }

            updateViewLayout()
        }


    /**
     * Sets the width and height of the thumb.this conflicts with the [thumbRadius]
     * ??????????????????
     * ?????????????????????thumb drawable
     *
     * @see R.attr.thumbWidth
     * @see R.attr.thumbHeight
     *
     * @param radius ????????????
     */
    fun setThumbWidthAndHeight(thumbWidth: Int, thumbHeight: Int, radius: Int = thumbRadius) {
        if ((this.thumbWidth == thumbWidth && this.thumbHeight == thumbHeight) || (thumbHeight < 0 && thumbWidth <= 0)) {
            return
        }
        if (thumbWidth >= 0) {
            this.thumbWidth = thumbWidth
        } else {
            this.thumbWidth = thumbRadius * 2
        }

        if (thumbHeight >= 0) {
            this.thumbHeight = thumbHeight
        } else {
            this.thumbHeight = thumbRadius * 2
        }

        if (radius != thumbRadius) {
            defaultThumbDrawable.shapeAppearanceModel =
                ShapeAppearanceModel.builder().setAllCorners(CornerFamily.ROUNDED, radius.toFloat()).build()
        }

        defaultThumbDrawable.setBounds(
            0,
            0,
            this.thumbWidth,
            this.thumbHeight
        )
        updateViewLayout()
    }


    /**
     * Sets the vertical offset of the thumb
     * ??????thumb??????????????????
     *
     * @see R.attr.thumbVOffset
     *
     * @param offset ?????????
     */
    fun setThumbVOffset(offset: Int) {
        if (offset == thumbVOffset) {
            return
        }
        thumbVOffset = offset
        postInvalidate()
    }

    /**
     * Sets whether the thumb within track bounds
     * ?????????????????????thumb??????track????????????????????????,thumb??????????????????????????????????????????
     * ??????????????????thumb???????????????track?????????????????????????????????????????????????????????
     *
     * @see R.attr.thumbWithinTrackBounds
     *
     * @param isInBounds thumb ????????????????????? track ????????????
     */
    fun setThumbWithinTrackBounds(isInBounds: Boolean) {

        isThumbWithinTrackBounds = isInBounds

        val offset = if (isInBounds) {
            //???????????????????????????thumb??????????????????????????????????????????
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

    /**
     * Sets the color of the thumb.
     *
     * @see R.attr.thumbColor
     */
    fun setThumbTintList(thumbColor: ColorStateList) {
        if (thumbColor == defaultThumbDrawable.fillColor) {
            return
        }
        defaultThumbDrawable.fillColor = thumbColor
        invalidate()
    }

    /**
     * Sets the color of the thumb text.
     *
     * @see R.attr.thumbTextColor
     */
    fun setThumbTextTintList(color: ColorStateList?) {
        if (color != null) {
            if (this::thumbTextColor.isInitialized && thumbTextColor == color) {
                return
            }
            thumbTextColor = color
            thumbTextPaint.color = getColorForState(thumbTextColor)
            invalidate()
        }
    }

    /**
     * Sets the text size of the thumb text.
     *
     * @see R.attr.thumbTextSize
     */
    fun setThumbTextSize(size: Float) {
        if (thumbTextPaint.textSize != size) {
            thumbTextPaint.textSize = size
            invalidate()
        }
    }

    fun setThumbTextBold(isBold: Boolean) {
        if (thumbTextPaint.isFakeBoldText != isBold) {
            thumbTextPaint.isFakeBoldText = isBold
            invalidate()
        }
    }


    fun setThumbCustomDrawable(@DrawableRes drawableResId: Int) {
        ContextCompat.getDrawable(context, drawableResId)?.also {
            setThumbCustomDrawable(it)
        }
    }

    fun setThumbCustomDrawable(drawable: Drawable) {
        customThumbDrawable = initializeCustomThumbDrawable(drawable)
        postInvalidate()
    }


    /**
     * Sets the color of the halo.
     * ?????????????????????????????????
     *
     * @see R.attr.haloColor
     */
    fun setHaloTintList(haloColor: ColorStateList) {
        if (this::haloColor.isInitialized && this.haloColor == haloColor) {
            return
        }

        this.haloColor = haloColor
        //v23??????????????????????????????????????????????????????
        if (!shouldDrawCompatHalo() && background is RippleDrawable) {
            (background as RippleDrawable).setColor(haloColor)
            return
        }

        haloPaint.apply {
            color = getColorForState(haloColor)
            alpha = HALO_ALPHA
        }

        invalidate()

    }

    /**
     * Sets the radius of the halo in pixels.
     * ????????????????????????????????????
     *
     * @see R.attr.haloRadius
     */
    fun setHaloRadius(@IntRange(from = 0) @Dimension radius: Int) {
        if (haloRadius == radius) {
            return
        }

        haloRadius = radius
        //v23???????????????????????????v23????????????hook ripple effect background???????????????
        if (!shouldDrawCompatHalo() && enableDrawHalo && background is RippleDrawable) {
            hookRippleRadius(background as RippleDrawable, haloRadius)
            return
        }
        postInvalidate()
    }

    /**
     * Sets the elevation of the thumb.
     *
     * @see R.attr.thumbElevation
     */
    fun setThumbElevation(elevation: Float) {
        defaultThumbDrawable.elevation = elevation

        thumbElevation = elevation
    }

    /**
     * Sets the stroke color for the thumbs
     *
     * @see R.attr.thumbStrokeColor
     */
    fun setThumbStrokeColor(thumbStrokeColor: ColorStateList?) {
        defaultThumbDrawable.strokeColor = thumbStrokeColor
        postInvalidate()
    }

    /**
     * Sets the stroke width for the thumb
     *
     * @see R.attr.thumbStrokeWidth
     */
    fun setThumbStrokeWidth(thumbStrokeWidth: Float) {
        defaultThumbDrawable.strokeWidth = thumbStrokeWidth
        postInvalidate()
    }

    /**
     * Sets the shadow width for the thumb
     *
     * @see R.attr.thumbShadowColor
     */
    fun setThumbShadowColor(@ColorInt shadowColor: Int) {
        defaultThumbDrawable.setShadowColor(shadowColor)
    }


    /**
     * Sets whether the halo should be draw
     * ??????????????????
     *
     * @see R.attr.enableDrawHalo
     *
     * @param enable True if this enable draw halo
     */
    fun setEnableDrawHalo(enable: Boolean) {
        enableDrawHalo = enable
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && haloDrawable == null && enable) {
            background = ContextCompat.getDrawable(context, R.drawable.halo_background)
            haloDrawable = background as RippleDrawable
        }
    }

    /**
     * Returns true if step mode enable
     * ???????????????????????????
     */
    fun enableStepMode(): Boolean {
        return stepSize > 0
    }

    /**
     * Update halo Hotspot coordinate
     *
     * ??????v23????????????????????????ripple effect??????
     */


    fun shouldDrawCompatHalo(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP || background !is RippleDrawable
    }


    @ColorInt
    fun getColorForState(colorStateList: ColorStateList): Int {
        return colorStateList.getColorForState(drawableState, colorStateList.defaultColor)
    }

    /**
     * ???????????????????????????????????????????????????????????????????????????????????????
     */
    private fun snapStepPos(pos: Float): Float {
        if (enableStepMode()) {
            val stepCount = ((valueTo - valueFrom) / stepSize).toInt()
            return (pos * stepCount).roundToInt() / stepCount.toFloat()
        }
        return pos
    }

    /**
     * ???????????????????????????????????????????????????value???
     * ??????????????????????????????????????????
     *
     * eg.
     * valueFrom = -40  valueTo = -20
     * valueFrom = -1  valueTo = 1
     * valueFrom = 1  valueTo = 100
     * valueFrom = 50  valueTo = 80
     *
     */
    private fun getValueByTouchPos(pos: Float): Float {
        val position = snapStepPos(pos)
        return position * (valueTo - valueFrom) + valueFrom
    }

    /**
     * ?????????????????????????????????????????????
     */
    private fun getTouchPosByX(touchX: Float): Float {
        return MathUtils.clamp((touchX - paddingLeft - trackInnerHPadding) / trackWidth, 0f, 1f)
    }

    /**
     * ?????????????????????????????????
     * ?????????????????????touch event???????????????
     */
    private fun isInVerticalScrollingContainer(): Boolean {
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


    private fun initializeCustomThumbDrawable(originalDrawable: Drawable): Drawable? {
        val drawable = originalDrawable.mutate()
        if (drawable != null) {
            adjustCustomThumbDrawableBounds(drawable)
        }
        return drawable
    }


    private fun adjustCustomThumbDrawableBounds(drawable: Drawable) {
        val thumbDiameter = thumbRadius * 2
        val originalWidth = drawable.intrinsicWidth
        val originalHeight = drawable.intrinsicHeight
        if (originalWidth == -1 && originalHeight == -1) {
            drawable.setBounds(0, 0, thumbDiameter, thumbDiameter)
        } else {
            val scaleRatio = thumbDiameter.toFloat() / max(originalWidth, originalHeight)
            drawable.setBounds(
                0, 0, (originalWidth * scaleRatio).toInt(), (originalHeight * scaleRatio).toInt()
            )
        }
    }


    /**
     * Start drag slider
     */
    private fun startTacking(event: MotionEvent) {
        isTackingStart = true
        onStartTacking()
    }

    /**
     * stop drag slider
     */
    private fun stopTacking(event: MotionEvent) {
        if (isTackingStart) {
            onStopTacking()
        }
        isTackingStart = false
        invalidate()
    }


    private fun updateHaloHotspot() {
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


    private fun trackTouchEvent(event: MotionEvent) {
        val touchPos = getTouchPosByX(event.x)
        val touchValue = getValueByTouchPos(touchPos)
        if (this.value != touchValue) {
            value = touchValue
            onValueChanged(value, true)
            updateHaloHotspot()
            invalidate()
        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) {
            return false
        }

        val currentX = event.x

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchDownX = currentX

                if (isInVerticalScrollingContainer()) {
                    //?????????????????????????????????down???????????????????????????
                } else {
                    parent.requestDisallowInterceptTouchEvent(true)
                    requestFocus()
                    isDragging = true
                    startTacking(event)
                    trackTouchEvent(event)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (!isDragging) {
                    if (isInVerticalScrollingContainer() && abs(currentX - touchDownX) < scaledTouchSlop) {
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
                    if (it.action == MotionEvent.ACTION_DOWN && isClickTouch(it, event)) {
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


    /**
     * Returns true if current touch event is click event
     *
     * @param startEvent ???????????????down??????
     * @param endEvent   ???????????????up??????
     */
    private fun isClickTouch(startEvent: MotionEvent, endEvent: MotionEvent): Boolean {
        val differenceX = abs(startEvent.x - endEvent.x)
        val differenceY = abs(startEvent.y - endEvent.y)
        return !(differenceX > scaledTouchSlop || differenceY > scaledTouchSlop)
    }

    private fun hookRippleRadius(drawable: RippleDrawable, radius: Int) {
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

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val sliderState = SavedState(superState)
        sliderState.value = value
        return sliderState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val sliderState = state as SavedState
        super.onRestoreInstanceState(sliderState.superState)
        value = sliderState.value
    }


    internal class SavedState : BaseSavedState {
        var value = 0f

        constructor(superState: Parcelable?) : super(superState) {}

        constructor(parcel: Parcel) : super(parcel) {
            value = parcel.readFloat()
        }


        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)
            parcel.writeFloat(value)
        }

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(parcel: Parcel): SavedState {
                return SavedState(parcel)
            }

            override fun newArray(size: Int): Array<SavedState?> {
                return arrayOfNulls(size)
            }
        }

    }


}