package com.litao.niftyslider.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.litao.niftyslider.Data
import com.litao.niftyslider.R
import com.litao.niftyslider.Utils
import com.litao.niftyslider.databinding.FragmentWeReadDemoBinding
import com.litao.slider.NiftySlider
import com.litao.slider.effect.WeReadEffect

/**
 * @author : litao
 * @date   : 2023/2/21 15:54
 */
class WeReadDemoFragment : Fragment() {
    private lateinit var binding: FragmentWeReadDemoBinding


    companion object {
        fun newInstance(): WeReadDemoFragment {
            return WeReadDemoFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentWeReadDemoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val stateArray = arrayOf(
            intArrayOf(-android.R.attr.state_enabled),
            intArrayOf(android.R.attr.state_enabled)
        )

        val activeTrackColor = Utils.setColorAlpha(ContextCompat.getColor(requireContext(), R.color.we_read_theme_color),0.1f)
        val inactiveTrackColor = Utils.setColorAlpha(ContextCompat.getColor(requireContext(), R.color.we_read_theme_color),0.05f)


        with(binding) {

            niftySlider.apply {
                effect = WeReadEffect(this)
                setTrackTintList(ColorStateList.valueOf(activeTrackColor))
                setTrackInactiveTintList(ColorStateList.valueOf(inactiveTrackColor))
            }


            niftySlider2.apply {
                effect = WeReadEffect(niftySlider2)
                setTrackTintList(ColorStateList.valueOf(activeTrackColor))
                setTrackInactiveTintList(ColorStateList.valueOf(inactiveTrackColor))
                setOnIntValueChangeListener { slider, value, fromUser ->
                    setThumbText(Data.weReadFontSizeMap[value].toString())
                }
            }
        }
    }
}