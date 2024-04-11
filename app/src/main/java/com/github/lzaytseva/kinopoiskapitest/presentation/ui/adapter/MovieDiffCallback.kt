package com.github.lzaytseva.kinopoiskapitest.presentation.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import com.github.lzaytseva.kinopoiskapitest.domain.model.Movie

object MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}