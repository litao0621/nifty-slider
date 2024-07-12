package com.litao.slider.effect

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import android.view.ViewGroup
import androidx.core.view.doOnAttach
import com.litao.slider.NiftySlider
import com.litao.slider.anim.SliderValueAnimation
import java.lang.Float.min

/**
 * @author : litao
 * @date   : 2023/5/30 10:37
 */
class ChartEffect(private val slider: NiftySlider) : BaseEffect() {

    private var chartPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)

    private var chartPath = Path()

    private val keyPoint = mutableListOf<PointF>()

    private val sliderAnimation = SliderValueAnimation()

    var chartMaxHeight = 64f
    private var heightFraction = 1f
    private var progress = 0f

    var chartColor = -1
        set(value) {
            field = value
            chartPaint.color = value
        }

    init {
        chartPaint.apply {
            strokeWidth = 2f
            chartColor = Color.WHITE
            style = Paint.Style.FILL
        }
        val parent = slider.parent
        if (parent is ViewGroup) {
            parent.clipChildren = false
        }

        sliderAnimation.apply {
            addUpdateListener {
                progress = getAnimatedValueAbsolute()
                slider.invalidate()
            }
        }

        slider.doOnAttach {
            if (slider.isRtl()) {
                keyPoint.reverse()
            }

        }
    }


    override fun onStartTacking(slider: NiftySlider) {
        super.onStartTacking(slider)
        checkParams(keyPoint)
        startAnim(false)
    }

    override fun onStopTacking(slider: NiftySlider) {
        super.onStopTacking(slider)
        startAnim(true)
    }

    override fun onDrawBefore(canvas: Canvas, trackRect: RectF, yCenter: Float) {
        super.onDrawBefore(canvas, trackRect, yCenter)
        if (progress != 0f) {
            createPath(trackRect, yCenter)
            canvas.drawPath(chartPath, chartPaint)
        }
    }


    private fun createPath(trackRect: RectF, yCenter: Float) {

        //转换为正式x坐标
        fun getRealX(x: Float): Float {
            return (x - slider.valueFrom) / (slider.valueTo - slider.valueFrom) * trackRect.width() + trackRect.left
        }
        //转化为正式y坐标
        fun getRealY(y: Float): Float {
            return yCenter - y * heightFraction - slider.trackHeight / 2f
        }


        chartPath.reset()
        var lastX = getRealX(0f)
        var lastY = getRealY(0f)

        chartPath.moveTo(lastX, lastY)

        keyPoint.forEachIndexed { index, pointF ->
            val realX = getRealX(pointF.x)
            val realY = getRealY(pointF.y * progress)

            //贝塞尔曲线控制点
            val x1 = (realX - lastX) / 2f + lastX
            val y1 = lastY
            val x2 = (realX - lastX) / 2f + lastX
            val y2 = realY

            chartPath.cubicTo(x1, y1, x2, y2, realX, realY)
            lastX = realX
            lastY = realY
        }
        chartPath.close()
    }


    /**
     * 更新关键点数据
     */
    fun updateKeyPoint(points: List<PointF>) {
        if (points.isNotEmpty()) {
            calculateHeightFraction(points)
            keyPoint.clear()
            keyPoint.addAll(points)
            keyPoint.add(PointF(slider.valueTo, 0f))
        }
    }


    private fun startAnim(isReversed: Boolean) {
        sliderAnimation.apply {
            if (isReversed) {
                reverse()
            } else {
                start()
            }
        }
    }

    /**
     * 计算高度压缩比例
     */
    private fun calculateHeightFraction(points: List<PointF>) {
        var maxValue = 0f
        var minValue = 0f
        points.forEach {
            if (it.y > maxValue) {
                maxValue = it.y
            }
            if (it.y < minValue) {
                minValue = it.y
            }
        }
        heightFraction = min(chartMaxHeight / maxValue, 1f)
    }


    /**
     * 检测参数合法性
     */
    private fun checkParams(points: List<PointF>) {
        points.forEach {
            if (it.x < slider.valueFrom || it.x > slider.valueTo) {
                error("Point x must be between the valueFrom and valueTo")
            }
            if (it.y < 0) {
                error("Point y must be greater than 0")
            }
        }
    }


}