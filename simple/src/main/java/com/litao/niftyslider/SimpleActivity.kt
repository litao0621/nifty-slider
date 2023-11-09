package com.litao.niftyslider

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.litao.niftyslider.databinding.ActivitySimpleBinding
import com.litao.niftyslider.fragment.*

/**
 * @author : litao
 * @date   : 2023/2/15 16:30
 */
class SimpleActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySimpleBinding

    companion object {
        const val EXTRA_ID = "id"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivitySimpleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView(savedInstanceState)
    }

    private fun initView(savedInstanceState: Bundle?) {
        val id = intent.getIntExtra(EXTRA_ID, 0)

        val fragment = when (id) {
            Data.ID_1 -> {
                BasicsDemoFragment.newInstance()
            }
            Data.ID_2 -> {
                M3StyleDemoFragment.newInstance()
            }
            Data.ID_3 -> {
                ScrollContainerDemoFragment.newInstance()
            }
            Data.ID_4 -> {
                WeReadDemoFragment.newInstance()
            }
            Data.ID_5 -> {
                ColorPickDemoFragment.newInstance()
            }
            Data.ID_6 -> {
                CustomThumbDemoFragment.newInstance()
            }
            Data.ID_7 -> {
                CustomThumbWithLottieDemoFragment.newInstance()
            }
            Data.ID_8 -> {
                TiktokStyleDemoFragment.newInstance()
            }
            Data.ID_9 -> {
                BiliBiliDemoFragment.newInstance()
            }
            Data.ID_10 -> {
                YouTubeDemoFragment.newInstance()
            }
            Data.ID_11 -> {
                YouTubeChartDemoFragment.newInstance()
            }
            Data.ID_12 -> {
                AdjustPositionDemoFragment.newInstance()
            }
            else -> {
                null
            }
        }

        if (fragment != null && savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container_view, fragment)
                .commit()
        }
    }

}