package com.litao.niftyslider.fragment

import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.litao.niftyslider.databinding.FragmentYoutubeChartDemoBinding
import com.litao.niftyslider.dp
import com.litao.slider.effect.ChartEffect

/**
 * @author : litao
 * @date   : 2023/5/30 10:42
 */
class YouTubeChartDemoFragment : Fragment() {

    private lateinit var binding: FragmentYoutubeChartDemoBinding


    companion object {
        fun newInstance(): YouTubeChartDemoFragment {
            return YouTubeChartDemoFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentYoutubeChartDemoBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){


            val chartEffect = ChartEffect(niftySlider).apply {
                chartColor = Color.parseColor("#33FFFFFF")
                chartMaxHeight = 28f.dp
                updateKeyPoint(mutableListOf(
                    PointF(0f,10f),
                    PointF(5f,50f),
                    PointF(10f,70f),
                    PointF(15f,130f),
                    PointF(20f,170f),
                    PointF(25f,70f),
                    PointF(30f,110f),
                    PointF(35f,20f),
                    PointF(40f,10f),
                    PointF(45f,110f),
                    PointF(50f,20f),
                    PointF(55f,100f),
                    PointF(60f,170f),
                    PointF(65f,120f),
                    PointF(70f,30f),
                    PointF(75f,80f),
                    PointF(80f,80f),
                    PointF(85f,180f),
                    PointF(90f,10f),
                    PointF(95f,180f),
                    PointF(100f,10f),
                ))
            }
            niftySlider.apply {
                effect = chartEffect
            }

        }
    }
}