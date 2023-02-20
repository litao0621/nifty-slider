package com.litao.niftyslider.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ColorStateListInflaterCompat
import androidx.fragment.app.Fragment
import com.litao.niftyslider.R
import com.litao.niftyslider.databinding.FragmentM3StyleDemoBinding

/**
 * @author : litao
 * @date   : 2023/2/16 11:12
 */
class M3StyleDemoFragment : Fragment() {

    private lateinit var binding: FragmentM3StyleDemoBinding


    companion object {
        fun newInstance(): M3StyleDemoFragment {
            return M3StyleDemoFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentM3StyleDemoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        with(binding) {
            niftySlider.apply {
                setTrackTintList(ContextCompat.getColorStateList(context, R.color.m3_demo_track_color)!!)
                setTrackInactiveTintList(
                    ContextCompat.getColorStateList(
                        context,
                        R.color.m3_demo_track_inactive_color
                    )!!
                )
                setThumbTintList(ContextCompat.getColorStateList(context, R.color.m3_demo_thumb_color)!!)
                setThumbShadowColor(ContextCompat.getColor(context, R.color.white))
            }
        }


    }
}