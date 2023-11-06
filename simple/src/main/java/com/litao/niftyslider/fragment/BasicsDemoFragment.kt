package com.litao.niftyslider.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.litao.niftyslider.databinding.FragmentBasicsDemoBinding
import com.litao.slider.BaseSlider
import com.litao.slider.NiftySlider

/**
 * @author : litao
 * @date   : 2023/2/16 16:24
 */
class BasicsDemoFragment : Fragment() {
    private lateinit var binding: FragmentBasicsDemoBinding


    companion object {

        private const val VALUE_TEXT = "current value :  \nint = %d , \nfloat = %f"

        fun newInstance(): BasicsDemoFragment {
            return BasicsDemoFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentBasicsDemoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            niftySlider.addOnValueChangeListener { slider, value, fromUser ->
                valueText.text = VALUE_TEXT.format(value.toInt(), value)
            }

            niftySlider.addOnSliderTouchStartListener {
                binding.stateText.text = "Start Tracking Touch"
            }
            niftySlider.addOnSliderTouchStopListener {
                binding.stateText.text = "Stop Tracking Touch"
            }

            niftySlider2.addOnValueChangeListener { slider, value, fromUser ->
                valueText.text = VALUE_TEXT.format(value.toInt(), value)
            }

            niftySlider2.addOnSliderTouchStartListener {
                binding.stateText.text = "Start Tracking Touch"
            }
            niftySlider2.addOnSliderTouchStopListener {
                binding.stateText.text = "Stop Tracking Touch"
            }

            enableSwitch.setOnCheckedChangeListener { button, isChecked ->
                if (isChecked){
                    button.text = "enable"
                }else{
                    button.text = "disable"
                }
                niftySlider.isEnabled = isChecked
                niftySlider2.isEnabled = isChecked
            }
        }

    }


}