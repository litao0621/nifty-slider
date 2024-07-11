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
import com.litao.niftyslider.databinding.FragmentTestBinding
import com.litao.niftyslider.dp
import com.litao.slider.thumb.DefaultThumbDrawable

/**
 * @author : litao
 * @date   : 2023/8/15 10:49
 */
class TestFragment  : Fragment() {

    private lateinit var binding: FragmentTestBinding

    companion object {
        fun newInstance(): TestFragment {
            return TestFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTestBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            niftySlider.apply {
                setTrackTintList(ColorStateList.valueOf(Color.WHITE))
                setTrackInactiveTintList(ColorStateList.valueOf(ColorUtils.setAlphaComponent(Color.WHITE,0x99)))
                setHaloTintList(ColorStateList.valueOf(ColorUtils.setAlphaComponent(Color.WHITE,0x33)))
                setThumbStrokeWidth(3f.dp)
                setThumbStrokeColor(ColorStateList.valueOf(Color.RED))
                setThumbShadowColor(Color.WHITE)
                setThumbElevation(50f.dp)
            }
        }

    }

}