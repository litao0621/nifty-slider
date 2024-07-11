package com.litao.niftyslider

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.color.MaterialColors
import com.litao.niftyslider.adapter.ItemStyleAdapter
import com.litao.niftyslider.databinding.ActivityMainBinding
import com.litao.niftyslider.model.StyleModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var mAdapter: ItemStyleAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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