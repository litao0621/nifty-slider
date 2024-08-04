package com.litao.niftyslider.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import com.litao.niftyslider.Data
import com.litao.niftyslider.databinding.FragmentVerticalSliderBinding
import com.litao.niftyslider.dp
import com.litao.niftyslider.fragment.M3StyleDemoFragment.Companion.TAG
import com.litao.slider.NiftySlider
import com.litao.slider.effect.AnimationEffect

/**
 * @author : litao
 * @date   : 2024/8/2 11:46
 */
class VerticalFragment : Fragment() {

    private lateinit var binding: FragmentVerticalSliderBinding


    companion object {
        fun newInstance(): VerticalFragment {
            return VerticalFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentVerticalSliderBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            val thumbColor = ColorUtils.compositeColors(ColorUtils.setAlphaComponent(Color.WHITE, 0x55), Color.BLACK)
            val trackColor = ColorUtils.setAlphaComponent(Color.WHITE, 0x33)
            val inactiveColor = ColorUtils.setAlphaComponent(Color.WHITE, 0x11)

            val animEffect = AnimationEffect(niftySlider2).apply {
                srcTrackHeight = 16.dp
                srcThumbHeight = 18.dp
                srcThumbWidth = 18.dp
                srcThumbRadius = 9.dp
                srcThumbColor = thumbColor
                srcTrackColor = trackColor
                srcInactiveTrackColor = inactiveColor

                targetTrackHeight = 24.dp
                targetThumbHeight = 28.dp
                targetThumbWidth = 28.dp
                targetThumbRadius = 14.dp
                targetThumbColor = Color.WHITE
                targetTrackColor = ColorUtils.setAlphaComponent(Color.WHITE, 0xDD)
                targetInactiveTrackColor = ColorUtils.setAlphaComponent(Color.WHITE, 0x33)

                animationListener = object : AnimationEffect.OnAnimationChangeListener {
                    override fun onEnd(slider: NiftySlider) {
//                        Toast.makeText(requireContext(), "do something on animation end", Toast.LENGTH_SHORT).show()
                    }
                }

                setInterpolator(FastOutLinearInInterpolator())
            }
//
            niftySlider2.apply {
                effect = animEffect
                setTrackTintList(ColorStateList.valueOf(trackColor))
                setTrackInactiveTintList(ColorStateList.valueOf(inactiveColor))
                setThumbTintList(ColorStateList.valueOf(thumbColor))
                setThumbShadowColor(Color.BLACK)
            }

        }
    }
}