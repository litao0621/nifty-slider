package com.litao.niftyslider.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.fragment.app.Fragment
import com.litao.niftyslider.R
import com.litao.niftyslider.databinding.FragmentCustomThumbDemoBinding
import com.litao.niftyslider.dp

/**
 * @author : litao
 * @date   : 2023/3/2 15:35
 */
class CustomThumbDemoFragment  : Fragment() {
    private lateinit var binding: FragmentCustomThumbDemoBinding

    companion object {
        fun newInstance(): CustomThumbDemoFragment {
            return CustomThumbDemoFragment()
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCustomThumbDemoBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        with(binding) {


            niftySlider.apply {
                setThumbCustomDrawable(R.drawable.custom_thumb2)
                setTrackTintList(ColorStateList.valueOf(Color.WHITE))
                setTrackInactiveTintList(ColorStateList.valueOf(ColorUtils.setAlphaComponent(Color.WHITE,0x99)))
                setHaloTintList(ColorStateList.valueOf(ColorUtils.setAlphaComponent(Color.WHITE,0x33)))

            }

            niftySlide2.apply {
                setThumbCustomDrawable(R.drawable.custom_thumb)
                setTrackTintList(ColorStateList.valueOf(Color.WHITE))
                setTrackInactiveTintList(ColorStateList.valueOf(ColorUtils.setAlphaComponent(Color.WHITE,0x99)))
                //vertical offset of thumb
                setThumbVOffset((-8).dp)
            }
        }


    }

}