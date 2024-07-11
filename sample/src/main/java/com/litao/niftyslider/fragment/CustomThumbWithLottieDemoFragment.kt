package com.litao.niftyslider.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import com.airbnb.lottie.RenderMode
import com.litao.niftyslider.databinding.FragmentCustomThumbWithLottieDemoBinding
import com.litao.niftyslider.dp
import com.litao.niftyslider.effect.LottieAnimationEffect
import com.litao.slider.effect.BaseEffect


/**
 * @author : litao
 * @date   : 2023/3/3 11:05
 */
class CustomThumbWithLottieDemoFragment  : Fragment() {

    private lateinit var binding: FragmentCustomThumbWithLottieDemoBinding


    companion object {
        fun newInstance(): CustomThumbWithLottieDemoFragment {
            return CustomThumbWithLottieDemoFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCustomThumbWithLottieDemoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val trackColor = Color.parseColor("#a051a3")
        val trackInactiveColor = Color.parseColor("#eae8ed")

        with(binding) {


            val animEffect = LottieAnimationEffect(niftySlider).apply {
                setAnimation("thumb_1.json")
                //Change the rendering mode if needed
                setRenderMode(RenderMode.HARDWARE)
            }

            val animEffect2 = LottieAnimationEffect(niftySlider2).apply {
                setAnimation("thumb_2.json")
                setRenderMode(RenderMode.HARDWARE)
            }

            niftySlider.apply {
                effect = animEffect
                setTrackTintList(ColorStateList.valueOf(trackColor))
                setTrackInactiveTintList(ColorStateList.valueOf(ColorUtils.setAlphaComponent(trackInactiveColor,0x99)))
                //vertical offset of thumb
                setThumbVOffset((-16).dp)
            }

            niftySlider2.apply {
                effect = animEffect2
                setTrackTintList(ColorStateList.valueOf(trackColor))
                setTrackInactiveTintList(ColorStateList.valueOf(ColorUtils.setAlphaComponent(trackInactiveColor,0x99)))
                //vertical offset of thumb
                setThumbVOffset((-8).dp)
            }

        }


    }

}