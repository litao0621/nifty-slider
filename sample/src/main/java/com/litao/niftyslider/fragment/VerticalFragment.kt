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
            val thumbColor = Color.WHITE
            val trackColor = Color.parseColor("#2962ff")
            val inactiveColor = Color.parseColor("#EEEEEE")

            val animEffect = AnimationEffect(niftySlider).apply {
                srcTrackHeight = 6.dp
                srcThumbHeight = 18.dp
                srcThumbWidth = 18.dp
                srcThumbRadius = 9.dp
                srcThumbColor = thumbColor
                srcTrackColor = trackColor
                srcInactiveTrackColor = inactiveColor

                targetTrackHeight = 24.dp
                targetThumbHeight = 22.dp
                targetThumbWidth = 22.dp
                targetThumbRadius = 11.dp
                targetThumbColor = thumbColor
                targetTrackColor = trackColor
                targetInactiveTrackColor = inactiveColor

                animationListener = object : AnimationEffect.OnAnimationChangeListener {
                    override fun onEnd(slider: NiftySlider) {
//                        Toast.makeText(requireContext(), "do something on animation end", Toast.LENGTH_SHORT).show()
                    }
                }

                setInterpolator(FastOutLinearInInterpolator())
            }
//
            niftySlider.apply {
                effect = animEffect
                setTrackTintList(ColorStateList.valueOf(trackColor))
                setTrackInactiveTintList(ColorStateList.valueOf(inactiveColor))
                setThumbTintList(ColorStateList.valueOf(thumbColor))
                setThumbShadowColor(Color.BLACK)
            }

        }
    }
}