package com.litao.niftyslider.fragment

import android.content.res.ColorStateList
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
import com.litao.niftyslider.dp
import com.litao.slider.effect.ITEffect

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

        val activeTrackColor =
            Utils.setColorAlpha(ContextCompat.getColor(requireContext(), R.color.we_read_theme_color), 0.1f)
        val inactiveTrackColor =
            Utils.setColorAlpha(ContextCompat.getColor(requireContext(), R.color.we_read_theme_color), 0.05f)
        val iconTintColor =
            Utils.setColorAlpha(ContextCompat.getColor(requireContext(), R.color.we_read_theme_color), 0.7f)


        with(binding) {

            niftySlider.apply {
                effect = ITEffect(this).apply {
                    setStartIcon(R.drawable.icon_brightness_down)
                    setEndIcon(R.drawable.icon_brightness_up)
                    startIconSize = 10.dp
                    endIconSize = 14.dp
                    startTintList = ColorStateList.valueOf(iconTintColor)
                    endTintList = ColorStateList.valueOf(iconTintColor)
                    startPadding = 12.dp
                    endPadding = 12.dp
                }
                setTrackTintList(ColorStateList.valueOf(activeTrackColor))
                setTrackInactiveTintList(ColorStateList.valueOf(inactiveTrackColor))
            }


            niftySlider2.apply {
                effect = ITEffect(this).apply {
                    setStartIcon(R.drawable.icon_font)
                    setEndIcon(R.drawable.icon_font)
                    startIconSize = 10.dp
                    endIconSize = 14.dp
                    startTintList = ColorStateList.valueOf(iconTintColor)
                    endTintList = ColorStateList.valueOf(iconTintColor)
                    startPadding = 12.dp
                    endPadding = 12.dp
                }
                setTrackTintList(ColorStateList.valueOf(activeTrackColor))
                setTrackInactiveTintList(ColorStateList.valueOf(inactiveTrackColor))
                addOnIntValueChangeListener { slider, value, fromUser ->
                    setThumbText(Data.weReadFontSizeMap[value].toString())
                }
            }

            niftySlider3.apply {
                effect = ITEffect(this).apply {
                    startText = "大"
                    endText = "小"
                    startTextSize = 12f.dp
                    endTextSize = 12f.dp
                    startTintList = ColorStateList.valueOf(iconTintColor)
                    endTintList = ColorStateList.valueOf(iconTintColor)
                    startPadding = 12.dp
                    endPadding = 12.dp
                }
                setTrackTintList(ColorStateList.valueOf(activeTrackColor))
                setTrackInactiveTintList(ColorStateList.valueOf(inactiveTrackColor))
            }
        }
    }
}