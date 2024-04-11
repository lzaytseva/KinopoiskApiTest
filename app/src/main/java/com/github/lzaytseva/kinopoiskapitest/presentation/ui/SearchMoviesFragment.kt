package com.github.lzaytseva.kinopoiskapitest.presentation.ui

import com.github.lzaytseva.kinopoiskapitest.databinding.FragmentSearchMoviesBinding
import com.github.lzaytseva.kinopoiskapitest.presentation.viewmodel.SearchMoviesViewModel
import com.github.lzaytseva.kinopoiskapitest.util.BaseFragment

class SearchMoviesFragment :
    BaseFragment<FragmentSearchMoviesBinding, SearchMoviesViewModel>(
        FragmentSearchMoviesBinding::inflate
    ) {
    override val viewModel: SearchMoviesViewModel
        get() = TODO("Not yet implemented")

    override fun onConfigureViews() {
        TODO("Not yet implemented")
    }

    override fun onSubscribe() {
        TODO("Not yet implemented")
    }
}