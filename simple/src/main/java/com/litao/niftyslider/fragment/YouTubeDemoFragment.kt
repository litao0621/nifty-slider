package com.litao.niftyslider.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import com.litao.niftyslider.databinding.FragmentYoutubeDemoBinding
import com.litao.niftyslider.dp
import com.litao.slider.NiftySlider
import com.litao.slider.effect.AnimationEffect
import kotlin.math.max

/**
 * @author : litao
 * @date   : 2023/3/17 10:40
 */
class YouTubeDemoFragment  : Fragment() {
    private lateinit var binding: FragmentYoutubeDemoBinding

    private var testTimer:CountDownTimer? = null

    companion object {
        fun newInstance(): YouTubeDemoFragment {
            return YouTubeDemoFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentYoutubeDemoBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val secondaryTrackColor = ColorUtils.setAlphaComponent(Color.WHITE, 0x33)
        val inactiveColor = ColorUtils.setAlphaComponent(Color.WHITE, 0x11)

        with(binding) {

            val animEffect = AnimationEffect(niftySlider).apply {
                animDuration = 300
                srcThumbRadius = 6.dp
                targetThumbRadius = 9.dp
            }

            val animEffect2 = AnimationEffect(niftySlide2).apply {
                animDuration = 300
                srcThumbRadius = 6.dp
                targetThumbRadius = 9.dp
            }

            niftySlider.apply {
                effect = animEffect
                setTrackSecondaryTintList(ColorStateList.valueOf(secondaryTrackColor))
                setTrackInactiveTintList(ColorStateList.valueOf(inactiveColor))
                niftySlider.hideThumb(delayMillis = 2000)
                niftySlider.setOnSliderTouchListener(object : NiftySlider.OnSliderTouchListener {
                    override fun onStartTrackingTouch(slider: NiftySlider) {
                        slider.showThumb(false)
                    }

                    override fun onStopTrackingTouch(slider: NiftySlider) {
                        slider.hideThumb(delayMillis = 2000)
                    }
                })
            }

            niftySlide2.apply {
                effect = animEffect2
                setTrackSecondaryTintList(ColorStateList.valueOf(secondaryTrackColor))
                setTrackInactiveTintList(ColorStateList.valueOf(inactiveColor))
                niftySlide2.hideThumb(delayMillis = 2000)
                niftySlide2.setOnSliderTouchListener(object : NiftySlider.OnSliderTouchListener {
                    override fun onStartTrackingTouch(slider: NiftySlider) {
                        slider.showThumb(false)
                    }

                    override fun onStopTrackingTouch(slider: NiftySlider) {
                        slider.hideThumb(delayMillis = 2000)
                    }
                })
            }
        }

//        playVideo()

    }


    /**
     * 模拟播放视频
     */
    private fun playVideo(){
        testTimer = object :CountDownTimer(Long.MAX_VALUE,1000){
            override fun onTick(p0: Long) {
                updateProgress()
            }

            override fun onFinish() {

            }

        }.start()
    }

    fun updateProgress(){
        with(binding) {
            niftySlider.apply {
                setValue(value + 1)
                setSecondaryValue(max(value, secondaryValue) + 4)

                if (value >= valueTo){
                    setValue(0f)
                    setSecondaryValue(0f)
                }
            }

            niftySlide2.apply {
                setValue(value + 1)
                setSecondaryValue(max(value, secondaryValue) + 4)

                if (value >= valueTo){
                    setValue(0f)
                    setSecondaryValue(0f)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        testTimer?.cancel()
    }
}