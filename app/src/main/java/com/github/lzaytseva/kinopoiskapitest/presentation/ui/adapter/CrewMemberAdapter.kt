package com.github.lzaytseva.kinopoiskapitest.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.github.lzaytseva.kinopoiskapitest.databinding.ItemCastBinding
import com.github.lzaytseva.kinopoiskapitest.domain.model.CrewMember

class CrewMemberAdapter : ListAdapter<CrewMember, CrewMemberAdapter.CrewMemberViewHolder>(CrewMemberDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrewMemberViewHolder {
        return CrewMemberViewHolder(
            ItemCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CrewMemberViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class CrewMemberViewHolder(private val binding: ItemCastBinding) : ViewHolder(binding.root) {
        fun bind(member: CrewMember) {
            setAvatar(member.photo)
            binding.tvName.text = member.name
            binding.tvRole.text = member.profession
        }


        private fun setAvatar(avatarUrl: String?) {
            avatarUrl?.let { url ->
                Glide.with(itemView)
                    .load(url)
                    .into(binding.ivAvatar)
            }
        }
    }

    object CrewMemberDiffCallback : DiffUtil.ItemCallback<CrewMember>() {
        override fun areItemsTheSame(oldItem: CrewMember, newItem: CrewMember): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CrewMember, newItem: CrewMember): Boolean {
            return oldItem == newItem
        }
    }
}