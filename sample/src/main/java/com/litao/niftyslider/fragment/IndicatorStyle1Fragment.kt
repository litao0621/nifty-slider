package com.litao.niftyslider.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.litao.niftyslider.R
import com.litao.niftyslider.databinding.FragmentDiscreteStyle1Binding
import com.litao.niftyslider.dp
import com.litao.slider.effect.IndicatorITEffect
import com.litao.slider.model.IndicatorIcon
import com.litao.slider.model.IndicatorText

/**
 * @author : litao
 * @date   : 2024/7/10 18:07
 */
class IndicatorStyle1Fragment : Fragment() {

    private lateinit var binding: FragmentDiscreteStyle1Binding


    companion object {
        const val TAG = "DiscreteStyle1Fragment"
        fun newInstance(): IndicatorStyle1Fragment {
            return IndicatorStyle1Fragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentDiscreteStyle1Binding.inflate(inflater)
        val arguments = arguments
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            // icon style
            val effect2 = IndicatorITEffect(niftySlider).apply {
                indicatorWidth = 3f.dp
                indicatorHeight = 16f.dp
                drawSpace = 12.dp
                iconDrawArray = mutableListOf(
                    IndicatorIcon(ContextCompat.getDrawable(requireActivity(), R.drawable.icon_font),12.dp,Color.parseColor("#b0bed5")),
                    IndicatorIcon(ContextCompat.getDrawable(requireActivity(), R.drawable.icon_font),16.dp,Color.parseColor("#b0bed5")),
                    IndicatorIcon(ContextCompat.getDrawable(requireActivity(), R.drawable.icon_font),18.dp,Color.parseColor("#b0bed5")),
                    IndicatorIcon(ContextCompat.getDrawable(requireActivity(), R.drawable.icon_font),20.dp,Color.parseColor("#b0bed5")),
                    IndicatorIcon(ContextCompat.getDrawable(requireActivity(), R.drawable.icon_font),22.dp,Color.parseColor("#b0bed5"))
                )
            }
            niftySlider2.apply {
                this.effect = effect2
            }


            //text style
            val effect = IndicatorITEffect(niftySlider).apply {
                textDrawArray = mutableListOf(
                    IndicatorText("华山",12f.dp, Color.parseColor("#B71C1C")),
                    IndicatorText("恒山",13f.dp, Color.parseColor("#880E4F")),
                    IndicatorText("泰山",14f.dp, Color.parseColor("#4A148C")),
                    IndicatorText("嵩山",15f.dp, Color.parseColor("#311B92")),
                    IndicatorText("衡山",16f.dp, Color.parseColor("#004D40")),
                )
            }
            niftySlider.apply {
                this.effect = effect
            }



        }


    }
}