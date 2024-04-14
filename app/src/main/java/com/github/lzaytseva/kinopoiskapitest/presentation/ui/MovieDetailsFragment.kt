package com.github.lzaytseva.kinopoiskapitest.presentation.ui

import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.github.lzaytseva.kinopoiskapitest.R
import com.github.lzaytseva.kinopoiskapitest.app.App
import com.github.lzaytseva.kinopoiskapitest.databinding.FragmentMovieDetailsBinding
import com.github.lzaytseva.kinopoiskapitest.domain.model.MovieDetails
import com.github.lzaytseva.kinopoiskapitest.domain.model.Review
import com.github.lzaytseva.kinopoiskapitest.presentation.state.MovieDetailsScreenState
import com.github.lzaytseva.kinopoiskapitest.presentation.ui.adapter.ActorAdapter
import com.github.lzaytseva.kinopoiskapitest.presentation.ui.adapter.CrewMemberAdapter
import com.github.lzaytseva.kinopoiskapitest.presentation.ui.adapter.ImageAdapter
import com.github.lzaytseva.kinopoiskapitest.presentation.ui.adapter.ReviewAdapter
import com.github.lzaytseva.kinopoiskapitest.presentation.viewmodel.MovieDetailsViewModel
import com.github.lzaytseva.kinopoiskapitest.util.BaseFragment
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.launch
import java.util.Locale

class MovieDetailsFragment :
    BaseFragment<FragmentMovieDetailsBinding, MovieDetailsViewModel>(
        FragmentMovieDetailsBinding::inflate
    ) {
    override val viewModel: MovieDetailsViewModel by viewModels {
        component.getViewModelFactory()
    }

    private var movieId = 0

    private val component by lazy {
        (requireActivity().application as App).component
            .getMovieDetailsComponentFactory()
            .create(movieId)
    }

    private val actorsAdapter = ActorAdapter()
    private val crewAdapter = CrewMemberAdapter()
    private val reviewAdapter = ReviewAdapter {
        showBottomSheet(it)
    }
    private val imageAdapter = ImageAdapter()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>


    private lateinit var toolbar: MaterialToolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireArguments().let {
            movieId = it.getInt(ARGS_ID)
        }
    }

    override fun onConfigureViews() {
        configureToolbar()
        initRecyclerViews()
        setDescriptionBtnsClickListener()
        initBottomSheet()
        setOnCloseReviewClickListener()
    }

    override fun onSubscribe() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    updateUi(it)
                }
            }
        }
    }

    private fun updateUi(state: MovieDetailsScreenState) {
        when (state) {
            is MovieDetailsScreenState.Content -> {
                showContent(state.details)
            }

            MovieDetailsScreenState.Error.NoInternet -> {
                showNoInternet()
            }

            MovieDetailsScreenState.Error.ServerError -> {
                showServerError()
            }

            is MovieDetailsScreenState.Error.UndefinedError -> {
                showUndefinedError()
            }

            MovieDetailsScreenState.Initial -> {
                // no-op
            }

            MovieDetailsScreenState.Loading -> {
                showViews(progressBarVisible = true)
            }
        }
    }

    private fun showContent(details: MovieDetails) {
        showViews(contentLayoutVisible = true)

        toolbar.title = details.name

        with(binding) {
            tvAgeRating.text = details.ageRating
            tvCountries.text = details.countries.addCommaIfNeeded(nextField = details.duration)
            tvDescription.text = details.description
            tvDuration.text = details.duration.addCommaIfNeeded(nextField = details.ageRating)
            tvGenres.text = details.genres
            tvOrigName.text = details.origName
            tvYear.text = details.year.addCommaIfNeeded(nextField = details.countries)
            details.posterUrl?.let {
                Glide.with(ivPoster)
                    .load(it)
                    .into(ivPoster)
            }
        }
        setRating(details.rating)
        setDataToRvs(details)
    }

    private fun showViews(
        progressBarVisible: Boolean = false,
        errorLayoutVisible: Boolean = false,
        contentLayoutVisible: Boolean = false
    ) {
        binding.errorLayout.isVisible = errorLayoutVisible
        binding.contentLayout.isVisible = contentLayoutVisible
        binding.progressBar.isVisible = progressBarVisible
    }

    private fun String?.addCommaIfNeeded(nextField: String?): String? {
        return if (nextField.isNullOrBlank()) {
            this
        } else {
            getString(R.string.string_with_comma, this)
        }
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
            ContextCompat.getColor(requireContext(), textColorResId)
        )
    }

    private fun setDataToRvs(details: MovieDetails) {
        if (details.actors.isNullOrEmpty()) {
            binding.rvActors.isVisible = false
            binding.tvActorsNoInformation.isVisible = true
        } else {
            actorsAdapter.submitList(details.actors)
            binding.rvActors.isVisible = true
            binding.tvActorsNoInformation.isVisible = false
        }

        if (details.crew.isNullOrEmpty()) {
            binding.rvCrew.isVisible = false
            binding.tvCrewNoInformation.isVisible = true
        } else {
            crewAdapter.submitList(details.crew)
            binding.rvCrew.isVisible = true
            binding.tvCrewNoInformation.isVisible = false
        }

        if (details.reviews.isNullOrEmpty()) {
            binding.rvReviews.isVisible = false
            binding.tvReviewsNoInformation.isVisible = true
        } else {
            reviewAdapter.submitList(details.reviews)
            binding.rvReviews.isVisible = true
            binding.tvReviewsNoInformation.isVisible = false

        }

        if (details.seasonsInfo.isNullOrBlank()) {
            binding.seasonsInfo.isVisible = true
            binding.tvSeasonsInfo.isVisible = false
            binding.tvSeasonsNoInformation.isVisible = true
        } else {
            binding.seasonsInfo.isVisible = true
            binding.tvSeasonsInfo.isVisible = true
            binding.tvSeasonsInfo.text = details.seasonsInfo
        }

        if (details.images.isNullOrEmpty()) {
            binding.rvReviews.isVisible = false
            binding.tvImagesNoInformation.isVisible = true
        } else {
            imageAdapter.submitList(details.images)
            binding.rvImages.isVisible = true
            binding.tvImagesNoInformation.isVisible = false
        }
    }


    private fun showNoInternet() {
        setErrorLayout(R.drawable.ic_no_internet, R.string.no_internet_error)
    }

    private fun showServerError() {
        setErrorLayout(R.drawable.ic_server_error, R.string.server_error)
    }

    private fun showUndefinedError() {
        setErrorLayout(R.drawable.ic_server_error, R.string.undefined_error)
    }

    private fun setErrorLayout(imageResId: Int, textResId: Int) {
        showViews(errorLayoutVisible = true)
        binding.ivError.setImageDrawable(
            ContextCompat.getDrawable(requireContext(), imageResId)
        )
        binding.tvError.text = getString(textResId)
    }

    private fun configureToolbar() {
        toolbar = binding.toolbar
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initRecyclerViews() {
        binding.rvActors.adapter = actorsAdapter
        binding.rvActors.layoutManager = GridLayoutManager(
            requireContext(),
            3,
            GridLayoutManager.HORIZONTAL,
            false
        )

        binding.rvCrew.adapter = crewAdapter
        binding.rvCrew.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        binding.rvReviews.adapter = reviewAdapter
        binding.rvReviews.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        binding.rvImages.adapter = imageAdapter
        binding.rvImages.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
    }

    private fun setDescriptionBtnsClickListener() {
        binding.tvShowMore.setOnClickListener {
            binding.tvDescription.maxLines = Int.MAX_VALUE
            binding.tvShowMore.isVisible = false
            binding.tvShowLess.isVisible = true
        }

        binding.tvShowLess.setOnClickListener {
            binding.tvDescription.maxLines = DESCRIPTION_LINES
            binding.tvShowMore.isVisible = true
            binding.tvShowLess.isVisible = false
        }
    }

    private fun initBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.reviewBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun showBottomSheet(review: Review) {
        binding.tvTitle.isVisible = !review.title.isNullOrBlank()
        binding.tvTitle.text = review.title
        binding.tvAuthorName.text = review.author
        binding.tvReview.text = review.review
        binding.tvPublicationDate.text = review.date
        setType(review.type)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun setType(type: String?) {
        val colorResId = when (type) {
            null -> R.color.review_card_background
            "Позитивный" -> R.color.high_rating
            "Негативный" -> R.color.low_rating
            else -> R.color.medium_rating
        }
        binding.reviewType.background = ContextCompat.getDrawable(requireContext(), colorResId)
    }

    private fun setOnCloseReviewClickListener() {
        binding.btnClose.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    companion object {
        private const val ARGS_ID = "movie_id"

        fun createArgs(movieId: Int): Bundle {
            return bundleOf(
                ARGS_ID to movieId
            )
        }

        private const val DESCRIPTION_LINES = 5
    }
}
