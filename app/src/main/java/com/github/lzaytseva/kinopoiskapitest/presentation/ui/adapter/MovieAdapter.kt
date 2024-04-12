package com.github.lzaytseva.kinopoiskapitest.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.github.lzaytseva.kinopoiskapitest.R
import com.github.lzaytseva.kinopoiskapitest.databinding.ItemMovieBinding
import com.github.lzaytseva.kinopoiskapitest.domain.model.Movie
import java.util.Locale

class MovieAdapter(
    private val onMovieClicked: ((Int) -> Unit)
) : ListAdapter<Movie, MovieAdapter.MovieViewHolder>(MovieDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = currentList[position]
        holder.bind(movie)
        holder.itemView.setOnClickListener {
            onMovieClicked.invoke(movie.id)
        }
    }

    class MovieViewHolder(private val binding: ItemMovieBinding) : ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            setRating(movie.ratingKp)
            binding.tvTitle.text = movie.ruName
            setOrigNameAndYear(movie.origName, movie.year)
            setCountryAndGenres(movie.countries, movie.genres)
            setPoster(movie.posterUrl)
        }

        private fun setRating(rating: Double?) {
            val textColorResId =
                when (rating) {
                    null, 0.0 -> R.color.medium_rating
                    in 0.0..4.9 -> R.color.low_rating
                    in 5.0..6.9 -> R.color.medium_rating
                    in 7.0..10.0 -> R.color.high_rating
                    else -> R.color.medium_rating
                }

            binding.tvKpRating.text = if (rating == null || rating == 0.0) {
                "-"
            } else {
                String.format(Locale.ENGLISH, "%.1f", rating)
            }

            binding.tvKpRating.setTextColor(
                ContextCompat.getColor(itemView.context, textColorResId)
            )
        }

        private fun setOrigNameAndYear(origName: String?, year: Int?) {
            val divider = if (origName.isNullOrBlank() || year == null) {
                EMPTY_STRING
            } else {
                DIVIDER_NAME
            }
            val text = itemView.context.getString(
                R.string.values_with_divider,
                origName ?: EMPTY_STRING,
                divider,
                year?.toString()
            )
            if (text.isBlank()) {
                binding.tvOrigNameAndYear.isVisible = false
            } else {
                binding.tvOrigNameAndYear.text = text
            }

        }

        private fun setCountryAndGenres(countries: String?, genres: String?) {
            val divider = if (countries.isNullOrBlank() || genres.isNullOrBlank()) {
                EMPTY_STRING
            } else {
                DIVIDER_COUNTRY
            }
            binding.tvCountriesAngGenres.text = itemView.context.getString(
                R.string.values_with_divider,
                countries ?: EMPTY_STRING,
                divider,
                genres ?: EMPTY_STRING
            )
        }

        private fun setPoster(posterUrl: String?) {
            posterUrl?.let { url ->
                Glide.with(itemView)
                    .load(url)
                    .into(binding.ivPoster)
            }
        }
    }

    companion object {
        private const val EMPTY_STRING = ""
        private const val DIVIDER_NAME = ", "
        private const val DIVIDER_COUNTRY = " • "
    }
}