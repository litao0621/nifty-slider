package com.litao.niftyslider.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import com.litao.niftyslider.databinding.FragmentBilibiliThumbDemoBinding
import com.litao.niftyslider.dp
import com.litao.niftyslider.drawable.BiliBiliDrawable
import com.litao.slider.NiftySlider
import com.litao.slider.effect.AnimationEffect

/**
 * @author : litao
 * @date   : 2023/3/7 11:08
 */
class BiliBiliDemoFragment : Fragment() {
    private lateinit var binding: FragmentBilibiliThumbDemoBinding

    companion object {
        fun newInstance(): BiliBiliDemoFragment {
            return BiliBiliDemoFragment()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentBilibiliThumbDemoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val trackColor = Color.parseColor("#ff6699")

        val customDrawable = BiliBiliDrawable()

        with(binding) {

            customDrawable.callback = niftySlider

            val animEffect = AnimationEffect(niftySlider).apply {
                animDuration = 300
                srcTrackHeight = 4.dp
                srcThumbRadius = 10.dp

                targetTrackHeight = 12.dp
                targetThumbRadius = 12.dp

                animationListener = object : AnimationEffect.OnAnimationChangeListener {
                    override fun onEnd(slider: NiftySlider) {
                        customDrawable.startBlinkAnim()
                    }
                }
            }

            niftySlider.apply {
                effect = animEffect
                setThumbCustomDrawable(customDrawable)
                setTrackTintList(ColorStateList.valueOf(trackColor))
                setTrackInactiveTintList(ColorStateList.valueOf(ColorUtils.setAlphaComponent(Color.WHITE, 0x99)))
                setHaloTintList(ColorStateList.valueOf(ColorUtils.setAlphaComponent(Color.WHITE, 0x33)))

            }

            niftySlider.addOnValueChangeListener { slider, value, fromUser ->
                customDrawable.currentStateValue = value
            }
        }
    }
}