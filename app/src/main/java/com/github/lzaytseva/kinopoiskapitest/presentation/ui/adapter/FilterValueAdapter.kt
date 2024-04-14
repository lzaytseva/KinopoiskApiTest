package com.github.lzaytseva.kinopoiskapitest.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.github.lzaytseva.kinopoiskapitest.databinding.ItemFilterValueBinding
import com.github.lzaytseva.kinopoiskapitest.domain.model.FilterValue

class FilterValueAdapter(
    private val onValueClicked: (FilterValue) -> Unit
) :
    ListAdapter<FilterValue, FilterValueAdapter.FilterValueViewHolder>(FilterValueDiffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterValueViewHolder {
        return FilterValueViewHolder(
            ItemFilterValueBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: FilterValueViewHolder, position: Int) {
        val currentValue = currentList[position]
        holder.bind(currentValue)
        holder.itemView.setOnClickListener {
            onValueClicked.invoke(currentValue)
        }
    }

    class FilterValueViewHolder(private val binding: ItemFilterValueBinding) :
        ViewHolder(binding.root) {
        fun bind(value: FilterValue) {
            binding.tvValue.text = value.name
        }
    }

    object FilterValueDiffCallback : DiffUtil.ItemCallback<FilterValue>() {
        override fun areItemsTheSame(oldItem: FilterValue, newItem: FilterValue): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: FilterValue, newItem: FilterValue): Boolean {
            return oldItem == newItem
        }
    }
}