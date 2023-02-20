package com.litao.niftyslider.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.litao.niftyslider.Data
import com.litao.niftyslider.adapter.SliderListAdapter
import com.litao.niftyslider.databinding.FragmentScrollContainerDemoBinding

/**
 * @author : litao
 * @date   : 2023/2/15 17:33
 */
class ScrollContainerDemoFragment : Fragment() {

    private lateinit var binding: FragmentScrollContainerDemoBinding

    private var mAdapter: SliderListAdapter? = null

    companion object {
        fun newInstance(): ScrollContainerDemoFragment {
            return ScrollContainerDemoFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentScrollContainerDemoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAdapter = SliderListAdapter()

        with(binding) {
            listView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = mAdapter
            }
        }

        mAdapter?.reloadList(Data.getTestData())


    }


}