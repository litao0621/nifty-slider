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

        private const val VALUE_TEXT = "current value :  int(%d) , float(%f)"

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
            niftySlider.setOnValueChangeListener { slider, value, fromUser ->
                valueText.text = VALUE_TEXT.format(value.toInt(), value)
            }

            niftySlider.setOnSliderTouchListener(object : NiftySlider.OnSliderTouchListener {
                override fun onStartTrackingTouch(slider: NiftySlider) {
                    Toast.makeText(context, "Start Tracking Touch", Toast.LENGTH_SHORT).show()
                }

                override fun onStopTrackingTouch(slider: NiftySlider) {
                    Toast.makeText(context, "Stop Tracking Touch", Toast.LENGTH_SHORT).show()
                }

            })

            niftySlider2.setOnValueChangeListener { slider, value, fromUser ->
                valueText.text = VALUE_TEXT.format(value.toInt(), value)
            }

            niftySlider2.setOnSliderTouchListener(object : NiftySlider.OnSliderTouchListener {
                override fun onStartTrackingTouch(slider: NiftySlider) {
                    Toast.makeText(context, "Start Tracking Touch", Toast.LENGTH_SHORT).show()
                }

                override fun onStopTrackingTouch(slider: NiftySlider) {
                    Toast.makeText(context, "Stop Tracking Touch", Toast.LENGTH_SHORT).show()
                }

            })

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