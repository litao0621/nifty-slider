package com.litao.niftyslider.fragment

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.ColorUtils
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.litao.niftyslider.databinding.FragmentTiktokStyleDemoBinding

/**
 * @author : litao
 * @date   : 2023/3/6 10:41
 */
class TiktokStyleDemoFragment : Fragment() {

    private lateinit var binding: FragmentTiktokStyleDemoBinding


    companion object {
        fun newInstance(): TiktokStyleDemoFragment {
            return TiktokStyleDemoFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTiktokStyleDemoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            //thumb为半透明颜色时可能会将track背景映射出来,需要按需根据自己的背景对颜色进行下转换
            val thumbColor = ColorUtils.compositeColors(ColorUtils.setAlphaComponent(Color.WHITE, 0x55),Color.BLACK)

            niftySlider.apply {
                setTrackTintList(ColorStateList.valueOf(ColorUtils.setAlphaComponent(Color.WHITE, 0x33)))
                setTrackInactiveTintList(ColorStateList.valueOf(ColorUtils.setAlphaComponent(Color.WHITE, 0x11)))
                setThumbTintList(ColorStateList.valueOf(thumbColor))
                setThumbShadowColor(Color.BLACK)
            }


            ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
                bottomGuide.setPadding(0,0,0,insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom)
                insets
            }
        }
    }

}