package com.github.lzaytseva.kinopoiskapitest.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.github.lzaytseva.kinopoiskapitest.databinding.ItemImageBinding

class ImageAdapter : ListAdapter<String, ImageAdapter.ImageViewHolder>(StringDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class ImageViewHolder(private val binding: ItemImageBinding) : ViewHolder(binding.root) {
        fun bind(url: String) {
            Glide.with(itemView)
                .load(url)
                .into(binding.ivImage)
        }
    }

    object StringDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}