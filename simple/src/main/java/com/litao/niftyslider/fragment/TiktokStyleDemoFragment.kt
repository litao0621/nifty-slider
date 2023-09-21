package com.litao.niftyslider.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.ColorUtils
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import com.litao.niftyslider.databinding.FragmentTiktokStyleDemoBinding
import com.litao.niftyslider.dp
import com.litao.slider.NiftySlider
import com.litao.slider.effect.AnimationEffect

/**
 * @author : litao
 * @date   : 2023/3/6 10:41
 */
class TiktokStyleDemoFragment : Fragment() {

    private lateinit var binding: FragmentTiktokStyleDemoBinding


    companion object {
        fun newInstance(): TiktokStyleDemoFragment {
            return TiktokStyleDemoFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTiktokStyleDemoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            //thumb为半透明颜色时可能会将track背景映射出来,需要按需根据自己的背景对颜色进行下转换
            val thumbColor = ColorUtils.compositeColors(ColorUtils.setAlphaComponent(Color.WHITE, 0x55), Color.BLACK)
            val trackColor = ColorUtils.setAlphaComponent(Color.WHITE, 0x33)
            val inactiveColor = ColorUtils.setAlphaComponent(Color.WHITE, 0x11)

            val animEffect = AnimationEffect(niftySlider).apply {
                srcTrackHeight = 3.dp
                srcThumbHeight = 6.dp
                srcThumbWidth = 6.dp
                srcThumbRadius = 3.dp
                srcThumbColor = thumbColor
                srcTrackColor = trackColor
                srcInactiveTrackColor = inactiveColor

                targetTrackHeight = 12.dp
                targetThumbHeight = 16.dp
                targetThumbWidth = 8.dp
                targetThumbRadius = 5.dp
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

            niftySlider.apply {
                effect = animEffect
                setTrackTintList(ColorStateList.valueOf(trackColor))
                setTrackInactiveTintList(ColorStateList.valueOf(inactiveColor))
                setThumbTintList(ColorStateList.valueOf(thumbColor))
                setThumbShadowColor(Color.BLACK)
            }

            bottomBarLayout.bind(niftySlider)

            ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
                bottomGuide.setPadding(0, 0, 0, insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom)
                insets
            }

            testCode()
        }
    }


    /**
     * Please ignore this code. it's all test code
     */
    private fun testCode() {
        with(binding) {
            tab1.setOnClickListener {
                Toast.makeText(requireActivity(), "${tab1.text} is clicked", Toast.LENGTH_SHORT).show()
            }
            tab2.setOnClickListener {
                Toast.makeText(requireActivity(), "${tab2.text} is clicked", Toast.LENGTH_SHORT).show()
            }
            tab3.setOnClickListener {
                Toast.makeText(requireActivity(), "${tab3.text} is clicked", Toast.LENGTH_SHORT).show()
            }
            tab4.setOnClickListener {
                Toast.makeText(requireActivity(), "${tab4.text} is clicked", Toast.LENGTH_SHORT).show()
            }
        }
    }

}