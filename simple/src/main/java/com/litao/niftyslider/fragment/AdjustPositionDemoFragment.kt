package com.litao.niftyslider.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.litao.niftyslider.databinding.FragmentAdjustPostionDemoBinding

/**
 * @author : litao
 * @date   : 2023/11/9 11:48
 */
class AdjustPositionDemoFragment : Fragment() {

    private lateinit var binding: FragmentAdjustPostionDemoBinding

    companion object {

        fun newInstance(): AdjustPositionDemoFragment {
            return AdjustPositionDemoFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAdjustPostionDemoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            niftySlider.setThumbShadowColor(Color.BLACK)
        }

    }


}