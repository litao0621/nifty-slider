package com.litao.niftyslider.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.litao.niftyslider.Utils
import com.litao.niftyslider.databinding.FragmentSlidingVerificationBinding
import com.litao.niftyslider.effect.VerificationEffect

/**
 * @author : litao
 * @date   : 2024/8/19 10:25
 */
class SlidingVerificationDemoFragment : Fragment() {
    private lateinit var binding: FragmentSlidingVerificationBinding


    companion object {
        const val TAG = "VerificationFragment"
        fun newInstance(): SlidingVerificationDemoFragment {
            return SlidingVerificationDemoFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSlidingVerificationBinding.inflate(inflater)
        val arguments = arguments
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        with(binding) {
            niftySlider.apply {
                effect = VerificationEffect(this).apply {
                    text = "请按动滑块，拖至最右"
                    textColor = Color.parseColor("#666666")
                    textSize = Utils.dpToPxF(18f)
                }
                addOnSliderTouchStopListener {
                    val crtValue = it.value
                    if ((1 - (it.valueTo - crtValue) / (valueTo - valueFrom)) > 0.95) {
                        Toast.makeText(requireActivity(),"Sliding valid",Toast.LENGTH_SHORT).show()
                        it.setValue(it.valueTo, true)
                    } else {
                        Toast.makeText(requireActivity(),"Sliding invalid",Toast.LENGTH_SHORT).show()
                        it.setValue(it.valueFrom, true)
                    }

                }
            }

        }
    }
}