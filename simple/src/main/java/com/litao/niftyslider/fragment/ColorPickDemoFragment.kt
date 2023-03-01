package com.litao.niftyslider.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.litao.niftyslider.Utils
import com.litao.niftyslider.databinding.FragmentColorPickStyleDemoBinding
import com.litao.slider.effect.ColorPickEffect

/**
 * @author : litao
 * @date   : 2023/2/28 17:21
 */
class ColorPickDemoFragment : Fragment() {

    private lateinit var binding: FragmentColorPickStyleDemoBinding

    companion object {
        fun newInstance(): ColorPickDemoFragment {
            return ColorPickDemoFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentColorPickStyleDemoBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        with(binding) {
            val colorEffect = ColorPickEffect(niftySlider)
            val alphaColorEffect = ColorPickEffect(niftySlider2)

            colorEffect.colorValueChangeListener = ColorPickEffect.OnColorValueChangeListener { slider, color, fromUser ->
                colorText.setBackgroundColor(color)
                val hexColor = Utils.toHexColorString(color)
                colorText.text = hexColor
                niftySlider.setThumbShadowColor(color)
                niftySlider.setThumbStrokeColor(ColorStateList.valueOf(color))

                niftySlider2.setThumbShadowColor(color)
                niftySlider2.setThumbStrokeColor(ColorStateList.valueOf(color))
                if (niftySlider2.value != niftySlider2.valueTo){
                    niftySlider2.setValue(niftySlider2.valueTo)
                }

                alphaColorEffect.updateColors(
                    intArrayOf(
                        Color.WHITE,
                        color
                    )
                )
            }

            alphaColorEffect.colorValueChangeListener = ColorPickEffect.OnColorValueChangeListener { slider, color, fromUser ->
                colorText.setBackgroundColor(color)
                val hexColor = Utils.toHexColorString(color)
                colorText.text = hexColor

                niftySlider2.setThumbShadowColor(color)
                niftySlider2.setThumbStrokeColor(ColorStateList.valueOf(color))
            }



            niftySlider.apply {
                effect = colorEffect
                setValue(50f)
            }

            niftySlider2.apply {
                effect = alphaColorEffect
            }
        }
    }

}