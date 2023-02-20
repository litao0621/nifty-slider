package com.litao.niftyslider.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.litao.niftyslider.Data
import com.litao.niftyslider.databinding.ItemStyleViewBinding
import com.litao.niftyslider.model.StyleModel

/**
 * @author : litao
 * @date   : 2023/2/15 16:41
 */
class ItemStyleAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: MutableList<StyleModel> = mutableListOf()

    var itemClickListener: OnItemClickListener? = null

    fun interface OnItemClickListener {
        fun onItemClick(position: Int, model: StyleModel)
    }

    fun reloadList(data: List<StyleModel>?) {
        list.clear()
        if (data != null && data.isNotEmpty()) {
            list.addAll(data)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemHolder(
            ItemStyleViewBinding.inflate(
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
            holder.update(currentPosition, model, itemClickListener)
        }
    }


    class ItemHolder(private val binding: ItemStyleViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun update(position: Int, model: StyleModel, itemClickListener: OnItemClickListener?) {
            binding.apply {
                title.text = model.describe
                itemLayout.setCardBackgroundColor(Color.parseColor(Data.colors.shuffled()[0]))
                itemLayout.setOnClickListener {
                    itemClickListener?.onItemClick(position, model)
                }
            }
        }
    }
}