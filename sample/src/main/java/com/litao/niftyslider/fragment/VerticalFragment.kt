package com.litao.niftyslider.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.litao.niftyslider.Data
import com.litao.niftyslider.databinding.FragmentVerticalSliderBinding
import com.litao.niftyslider.fragment.M3StyleDemoFragment.Companion.TAG

/**
 * @author : litao
 * @date   : 2024/8/2 11:46
 */
class VerticalFragment : Fragment() {

    private lateinit var binding: FragmentVerticalSliderBinding


    companion object {
        fun newInstance(): VerticalFragment {
            return VerticalFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentVerticalSliderBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            niftySlider2.apply {
                addOnIntValueChangeListener { slider, value, fromUser ->
                    setThumbText(Data.weReadFontSizeMap[value].toString())
                }
            }

        }
    }
}