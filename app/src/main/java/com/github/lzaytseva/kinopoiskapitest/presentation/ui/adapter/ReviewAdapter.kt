package com.github.lzaytseva.kinopoiskapitest.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.github.lzaytseva.kinopoiskapitest.R
import com.github.lzaytseva.kinopoiskapitest.databinding.ItemReviewBinding
import com.github.lzaytseva.kinopoiskapitest.domain.model.Review

class ReviewAdapter(
    private val onReviewClicked: (Review) -> Unit
) : ListAdapter<Review, ReviewAdapter.ReviewViewHolder>(ReviewDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewViewHolder(
            ItemReviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = currentList[position]
        holder.bind(review)
        holder.itemView.setOnClickListener {
            onReviewClicked.invoke(review)
        }

    }

    class ReviewViewHolder(private val binding: ItemReviewBinding) : ViewHolder(binding.root) {
        fun bind(review: Review) {
            binding.tvTitle.isVisible = !review.title.isNullOrBlank()
            binding.tvTitle.text = review.title
            binding.tvAuthorName.text = review.author
            binding.tvReview.text = review.review
            binding.tvPublicationDate.text = review.date
            setType(review.type)
        }

        private fun setType(type: String?) {
            val colorResId = when (type) {
                null -> R.color.review_card_background
                "Позитивный" -> R.color.high_rating
                "Негативный" -> R.color.low_rating
                else -> R.color.medium_rating
            }
            binding.reviewType.background = ContextCompat.getDrawable(itemView.context, colorResId)
        }


    }

    object ReviewDiffCallback : DiffUtil.ItemCallback<Review>() {
        override fun areItemsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Review, newItem: Review): Boolean {
            return oldItem == newItem
        }
    }
}