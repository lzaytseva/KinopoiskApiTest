package com.github.lzaytseva.kinopoiskapitest.presentation.ui

import com.github.lzaytseva.kinopoiskapitest.databinding.FragmentMovieDetailsBinding
import com.github.lzaytseva.kinopoiskapitest.presentation.viewmodel.MovieDetailsViewModel
import com.github.lzaytseva.kinopoiskapitest.util.BaseFragment

class MovieDetailsFragment :
    BaseFragment<FragmentMovieDetailsBinding, MovieDetailsViewModel>(
        FragmentMovieDetailsBinding::inflate
    ) {
    override val viewModel: MovieDetailsViewModel
        get() = TODO("Not yet implemented")

    override fun onConfigureViews() {
        TODO("Not yet implemented")
    }

    override fun onSubscribe() {
        TODO("Not yet implemented")
    }
}