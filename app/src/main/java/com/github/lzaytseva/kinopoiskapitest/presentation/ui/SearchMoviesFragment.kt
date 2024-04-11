package com.github.lzaytseva.kinopoiskapitest.presentation.ui

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.lzaytseva.kinopoiskapitest.R
import com.github.lzaytseva.kinopoiskapitest.app.App
import com.github.lzaytseva.kinopoiskapitest.databinding.FragmentSearchMoviesBinding
import com.github.lzaytseva.kinopoiskapitest.domain.model.Movie
import com.github.lzaytseva.kinopoiskapitest.presentation.state.MoviesSearchScreenState
import com.github.lzaytseva.kinopoiskapitest.presentation.state.MoviesSearchSideEffect
import com.github.lzaytseva.kinopoiskapitest.presentation.ui.adapter.MovieAdapter
import com.github.lzaytseva.kinopoiskapitest.presentation.viewmodel.SearchMoviesViewModel
import com.github.lzaytseva.kinopoiskapitest.util.BaseFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class SearchMoviesFragment :
    BaseFragment<FragmentSearchMoviesBinding, SearchMoviesViewModel>(
        FragmentSearchMoviesBinding::inflate
    ) {
    override val viewModel: SearchMoviesViewModel by viewModels {
        component.getViewModelFactory()
    }

    private val component by lazy {
        (requireActivity().application as App).component
    }

    private val adapter = MovieAdapter { movieId ->
        // TODO
    }

    override fun onConfigureViews() {
        initRecyclerView()
    }

    override fun onSubscribe() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    updateUi(it)
                }
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.sideEffects.collect {
                    handleSideEffect(it)
                }
            }
        }
    }

    private fun updateUi(state: MoviesSearchScreenState) {
        when (state) {
            MoviesSearchScreenState.Loading -> showLoading()

            is MoviesSearchScreenState.Content -> showContent(state.movies)

            is MoviesSearchScreenState.Error.NoInternet -> showNoInternet()

            is MoviesSearchScreenState.Error.ServerError -> showServerError()

            is MoviesSearchScreenState.Error.UndefinedError -> showUndefinedError()

            MoviesSearchScreenState.EmptyResult -> showEmptyResult()

            MoviesSearchScreenState.LoadingNextPage -> showLoadingNextPage()

            MoviesSearchScreenState.Initial -> {}
        }
    }

    private fun handleSideEffect(sideEffect: MoviesSearchSideEffect) {
        when (sideEffect) {
            MoviesSearchSideEffect.NoInternet -> showNoInternet(isLoadingNextPage = true)
            MoviesSearchSideEffect.ServerError -> showServerError(isLoadingNextPage = true)
            is MoviesSearchSideEffect.UndefinedError -> showUndefinedError(isLoadingNextPage = true)
        }
    }

    private fun showLoading() {
        showViews(progressBarVisible = true)
    }

    private fun showContent(movies: List<Movie>) {
        showViews(rvVisible = true)
        adapter.submitList(movies)
    }

    private fun showEmptyResult() {
        setErrorLayout(R.drawable.ic_nothing_found, R.string.nothing_found)
    }

    private fun showLoadingNextPage() {
        showViews(rvVisible = true, progressBarVisible = true)
    }

    private fun showNoInternet(isLoadingNextPage: Boolean = false) {
        if (isLoadingNextPage) {
            showErrorPopup(R.string.no_internet_error)
        } else {
            setErrorLayout(R.drawable.ic_no_internet, R.string.no_internet_error)
        }
    }

    private fun showServerError(isLoadingNextPage: Boolean = false) {
        if (isLoadingNextPage) {
            showErrorPopup(R.string.server_error)
        } else {
            setErrorLayout(R.drawable.ic_server_error, R.string.server_error)
        }
    }

    private fun showUndefinedError(isLoadingNextPage: Boolean = false) {
        if (isLoadingNextPage) {
            showErrorPopup(R.string.undefined_error)
        } else {
            setErrorLayout(R.drawable.ic_server_error, R.string.undefined_error)
        }
    }

    private fun showErrorPopup(textResId: Int) {
        binding.progressBar.isVisible = false
        val text = getString(textResId)
        showSnackbar(text)
    }

    private fun showSnackbar(text: String) {
        val snackbar =
            Snackbar.make(requireView(), text, Snackbar.LENGTH_SHORT)
        snackbar.setBackgroundTint(
            ContextCompat.getColor(
                requireContext(),
                R.color.snackbar_bg
            )
        )
        snackbar.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.snackbar_text_color
            )
        )
        val textView =
            snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        snackbar.show()
    }

    private fun setErrorLayout(imageResId: Int, textResId: Int) {
        showViews(errorLayoutVisible = true)
        binding.ivError.setImageDrawable(
            ContextCompat.getDrawable(requireContext(), imageResId)
        )
        binding.tvError.text = getString(textResId)
    }

    private fun showViews(
        rvVisible: Boolean = false,
        progressBarVisible: Boolean = false,
        errorLayoutVisible: Boolean = false
    ) {
        binding.rvMovies.isVisible = rvVisible
        binding.progressBar.isVisible = progressBarVisible
        binding.errorLayout.isVisible = errorLayoutVisible
    }

    private fun initRecyclerView() {
        binding.rvMovies.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMovies.adapter = adapter
        binding.rvMovies.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (dy > 0) {
                        with(binding.rvMovies) {
                            val pos =
                                (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                            val itemsCount = adapter!!.itemCount
                            if (pos >= itemsCount - 1) {
                                viewModel.loadNextPage()
                            }
                        }
                    }
                }
            }
        )
    }
}