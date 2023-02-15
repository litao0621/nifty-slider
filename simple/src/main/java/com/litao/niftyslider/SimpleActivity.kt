package com.litao.niftyslider

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.litao.niftyslider.databinding.ActivitySimpleBinding
import com.litao.niftyslider.fragment.ScrollContainerDemoFragment

/**
 * @author : litao
 * @date   : 2023/2/15 16:30
 */
class SimpleActivity: AppCompatActivity() {

    private lateinit var binding: ActivitySimpleBinding

    companion object{
        const val EXTRA_ID = "id"

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySimpleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView(){
        val id = intent.getIntExtra(EXTRA_ID,0)
        var fragment:Fragment? = null

        when(id){
            Data.ID_1 -> {

            }
            Data.ID_2 -> {
                fragment = ScrollContainerDemoFragment.newInstance()
            }
        }
        if (fragment != null) {
            supportFragmentManager.beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_container_view, fragment)
                .commit()
        }
    }

}