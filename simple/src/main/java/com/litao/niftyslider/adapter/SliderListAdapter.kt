package com.litao.niftyslider.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.litao.niftyslider.databinding.ItemSliderViewBinding
import com.litao.niftyslider.model.SliderModel
import com.litao.slider.NiftySlider

/**
 * @author : litao
 * @date   : 2023/2/15 18:15
 */
class SliderListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: MutableList<SliderModel> = mutableListOf()

    fun reloadList(data: List<SliderModel>?) {
        list.clear()
        if (data != null && data.isNotEmpty()) {
            list.addAll(data)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemHolder(
            ItemSliderViewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentPosition = holder.adapterPosition
        val model = list[currentPosition]
        if (holder is ItemHolder) {
            holder.update(currentPosition, model)
        }
    }


    class ItemHolder(private val binding: ItemSliderViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun update(position: Int, model: SliderModel) {
            binding.apply {
                niftySlider.setValue(model.value)
                niftySlider.addOnSliderTouchStopListener {
                    model.value = it.value
                }
            }
        }
    }

}