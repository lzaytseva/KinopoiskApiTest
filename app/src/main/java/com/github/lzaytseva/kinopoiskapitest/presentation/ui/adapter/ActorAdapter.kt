package com.github.lzaytseva.kinopoiskapitest.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.github.lzaytseva.kinopoiskapitest.databinding.ItemCastBinding
import com.github.lzaytseva.kinopoiskapitest.domain.model.Actor

class ActorAdapter : ListAdapter<Actor, ActorAdapter.ActorViewHolder>(ActorDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActorViewHolder {
        return ActorViewHolder(
            ItemCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ActorViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class ActorViewHolder(private val binding: ItemCastBinding) : ViewHolder(binding.root) {
        fun bind(actor: Actor) {
            setAvatar(actor.photo)
            binding.tvName.text = actor.name
            binding.tvRole.text = actor.role
        }


        private fun setAvatar(avatarUrl: String?) {
            avatarUrl?.let { url ->
                Glide.with(itemView)
                    .load(url)
                    .into(binding.ivAvatar)
            }
        }
    }

    object ActorDiffCallback : DiffUtil.ItemCallback<Actor>() {
        override fun areItemsTheSame(oldItem: Actor, newItem: Actor): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Actor, newItem: Actor): Boolean {
            return oldItem == newItem
        }
    }
}