package com.litao.niftyslider

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.litao.niftyslider.adapter.ItemStyleAdapter
import com.litao.niftyslider.databinding.ActivityMainBinding
import java.util.Locale


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var mAdapter: ItemStyleAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val locale = Locale("fa_IR") //fa_IR是波斯语（伊朗）的代码
        Locale.setDefault(locale)
        val config: Configuration = Configuration()
        config.locale = locale
        val dm = resources.displayMetrics
        resources.updateConfiguration(config, dm)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()

    }

    private fun initView() {
        mAdapter = ItemStyleAdapter().apply {
            itemClickListener = ItemStyleAdapter.OnItemClickListener { _, model ->
                startActivity(
                    Intent(
                        this@MainActivity,
                        SimpleActivity::class.java
                    ).apply {
                        putExtra(SimpleActivity.EXTRA_ID, model.id)
                    }
                )
            }
        }

        with(binding) {
            listView.apply {
                layoutManager = GridLayoutManager(this@MainActivity, 2)
                adapter = mAdapter
            }
        }

        mAdapter?.reloadList(Data.styleList)
    }
}