package com.github.lzaytseva.kinopoiskapitest.presentation.ui

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.lzaytseva.kinopoiskapitest.R
import com.github.lzaytseva.kinopoiskapitest.app.App
import com.github.lzaytseva.kinopoiskapitest.databinding.FragmentSearchMoviesBinding
import com.github.lzaytseva.kinopoiskapitest.presentation.state.MoviesSearchScreenState
import com.github.lzaytseva.kinopoiskapitest.presentation.state.MoviesSearchSideEffect
import com.github.lzaytseva.kinopoiskapitest.presentation.ui.adapter.MovieAdapter
import com.github.lzaytseva.kinopoiskapitest.presentation.viewmodel.SearchMode
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
        gotBackFromDetails = true
        findNavController().navigate(
            R.id.action_searchMoviesFragment_to_movieDetailsFragment,
            MovieDetailsFragment.createArgs(movieId)
        )
    }

    private var isFiltersApplied = false
    private var gotBackFromDetails = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isFiltersApplied = arguments?.getBoolean(ARG_FILTERS_APPLIED) ?: false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (isFiltersApplied && !gotBackFromDetails) {
            viewModel.loadSearchMoviesByParams()
        } else if (gotBackFromDetails){
            return
        } else {
            viewModel.loadAllMovies()
        }
    }

    override fun onConfigureViews() {
        initRecyclerView()
        setTextWatcher()
        setupEditorActionListener()
        setEndIconClickListener()
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

            is MoviesSearchScreenState.Content -> showContent(state)

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

    private fun showContent(state: MoviesSearchScreenState.Content) {
        showViews(rvVisible = true)
        adapter.submitList(state.movies)
        when (state.searchMode) {
            SearchMode.AllMovies -> binding.allMoviesLabel.text =
                getString(R.string.all_movies_label)

            else -> binding.allMoviesLabel.text = getString(R.string.search_result_label)
        }
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
        binding.allMoviesLabel.isVisible = rvVisible
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

    private fun setTextWatcher() {
        binding.etSearch.doAfterTextChanged {
            setEndIconClickListener()
            search()
        }
    }

    private fun setupEditorActionListener() {
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                setEndIconClickListener()
                search()
            }
            false
        }
    }

    private fun search() {
        val searchQuery = binding.etSearch.text?.toString().orEmpty()
        viewModel.searchMoviesByTitle(changedText = searchQuery)

    }

    private fun setEndIconClickListener() {
        val isTextFieldEmpty = binding.etSearch.text.isNullOrBlank()
        binding.tilSearch.endIconDrawable = ContextCompat.getDrawable(
            requireContext(),
            getEndIconId(isTextFieldEmpty)
        )

        if (!isTextFieldEmpty) {
            binding.tilSearch.setEndIconOnClickListener {
                binding.etSearch.text?.clear()
                viewModel.loadAllMovies()
            }
        } else {
            binding.tilSearch.setEndIconOnClickListener {
                findNavController().navigate(R.id.action_searchMoviesFragment_to_fragmentFilters)
            }
        }
    }

    private fun getEndIconId(isTextFieldEmpty: Boolean): Int {
        return if (isTextFieldEmpty) {
            R.drawable.ic_filters
        } else {
            R.drawable.ic_close
        }
    }

    companion object {
        private const val ARG_FILTERS_APPLIED = "filters_applied_key"

        fun createArgs(applied: Boolean): Bundle {
            return bundleOf(
                ARG_FILTERS_APPLIED to applied
            )
        }
    }
}
