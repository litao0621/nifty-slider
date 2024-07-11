package com.litao.niftyslider.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.litao.niftyslider.databinding.FragmentM3StyleDemoBinding

/**
 * @author : litao
 * @date   : 2023/2/16 11:12
 */
class M3StyleDemoFragment : Fragment() {

    private lateinit var binding: FragmentM3StyleDemoBinding


    companion object {
        const val TAG = "M3StyleDemoFragment"
        fun newInstance(): M3StyleDemoFragment {
            return M3StyleDemoFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentM3StyleDemoBinding.inflate(inflater)
        val arguments = arguments
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        with(binding) {
            niftySlider.apply {
                addOnProgressAnimEndListener {
                    Log.d(TAG, "target value is ${it.value}")
                }

                addOnValueChangeListener { slider, value, fromUser ->
                    Log.d(TAG, "current value is ${slider.value}")
                }
            }
        }


    }
}