package com.github.lzaytseva.kinopoiskapitest.presentation.ui

import android.view.View
import android.widget.TextView
import androidx.activity.addCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.lzaytseva.kinopoiskapitest.R
import com.github.lzaytseva.kinopoiskapitest.app.App
import com.github.lzaytseva.kinopoiskapitest.data.exception.model.ErrorEntity
import com.github.lzaytseva.kinopoiskapitest.databinding.FragmentFiltersBinding
import com.github.lzaytseva.kinopoiskapitest.domain.model.Filters
import com.github.lzaytseva.kinopoiskapitest.presentation.state.FiltersScreenSideEffect
import com.github.lzaytseva.kinopoiskapitest.presentation.state.FiltersScreenState
import com.github.lzaytseva.kinopoiskapitest.presentation.ui.adapter.FilterValueAdapter
import com.github.lzaytseva.kinopoiskapitest.presentation.viewmodel.FiltersViewModel
import com.github.lzaytseva.kinopoiskapitest.util.BaseFragment
import com.google.android.material.slider.RangeSlider
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class FiltersFragment :
    BaseFragment<FragmentFiltersBinding, FiltersViewModel>(
        FragmentFiltersBinding::inflate
    ) {
    override val viewModel: FiltersViewModel by viewModels {
        component.getViewModelFactory()
    }

    private val component by lazy {
        (requireActivity().application as App).component
    }

    private val valueAdapter = FilterValueAdapter {
        viewModel.saveFilterValue(it)
    }

    private val yearDialog = YearPickerDialog {
        viewModel.saveDateRange(it)
    }


    override fun onConfigureViews() {
        initFilterValuesRV()
        setChooseCountryClickListener()
        setGenresClickListener()
        setBtnBackClickListener()
        addOnBackPressedCallback()
        setOnYearClickListener()
        setOnKpRatingRangeChanged()
        setOnShowButtonClickListener()
        setOnResetBtnClickListener()
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

    private fun updateUi(state: FiltersScreenState) {
        when (state) {
            is FiltersScreenState.FilterValues -> {
                showValues(state)
            }

            FiltersScreenState.Initial -> {
                // no-op
            }

            is FiltersScreenState.CurrentFilters -> {
                setFilters(state.filters)
            }
        }
    }

    private fun setFilters(filters: Filters) {
        binding.tvHeader.text = getString(R.string.filters_header)
        binding.layoutFilters.isVisible = true
        binding.rvItems.isVisible = false
        binding.tvGenre.text = filters.genre ?: getString(R.string.any_m)
        binding.tvCountry.text = filters.country ?: getString(R.string.any_f)
        binding.tvRating.text = filters.ratingKp ?: getString(R.string.label_doesnt_matter)
        binding.sliderKpRating.values = filters.ratingKp?.let {
            listOf(
                it.substringBefore('-').toFloat(),
                it.substringAfter('-').toFloat()
            )
        } ?: listOf(0.0f, 10.0f)
        binding.tvYear.text = filters.year ?: getString(R.string.any_m)
        when (filters.type) {
            getString(R.string.movie) -> binding.toggleButton.check(R.id.btn_movies)
            getString(R.string.tv_series) -> binding.toggleButton.check(R.id.btn_tv_series)
            else -> binding.toggleButton.check(R.id.btn_all)
        }
    }

    private fun showValues(state: FiltersScreenState.FilterValues) {
        binding.tvHeader.text = state.title
        binding.layoutFilters.isVisible = false
        binding.rvItems.isVisible = true
        valueAdapter.submitList(state.values)
    }

    private fun handleSideEffect(sideEffect: FiltersScreenSideEffect) {
        when (sideEffect) {
            is FiltersScreenSideEffect.FailedLoadingValues -> {
                val message = when (sideEffect.error) {
                    ErrorEntity.NoInternet -> getString(R.string.no_internet_error)
                    ErrorEntity.ServerError -> getString(R.string.server_error)
                    is ErrorEntity.UndefinedError -> getString(R.string.undefined_error)
                }
                showSnackbar(message)
            }

            FiltersScreenSideEffect.NavigateToSearchScreen -> {
                findNavController().popBackStack()
            }
        }
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

    private fun initFilterValuesRV() {
        binding.rvItems.layoutManager = LinearLayoutManager(requireContext())
        binding.rvItems.adapter = valueAdapter
    }

    private fun setChooseCountryClickListener() {
        binding.tvCountry.setOnClickListener {
            viewModel.onCountryFieldClicked()
        }
    }

    private fun setGenresClickListener() {
        binding.tvGenre.setOnClickListener {
            viewModel.onGenresFieldClicked()
        }
    }

    private fun setBtnBackClickListener() {
        binding.btnBack.setOnClickListener {
            viewModel.onBackButtonClicked()
        }
    }

    private fun addOnBackPressedCallback() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.onBackButtonClicked()
        }
    }

    private fun setOnYearClickListener() {
        binding.tvYear.setOnClickListener {
            yearDialog.show(requireActivity().supportFragmentManager, "tag")
        }
    }

    private fun setOnKpRatingRangeChanged() {
        binding.sliderKpRating.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            override fun onStartTrackingTouch(p0: RangeSlider) {
                // no-op
            }

            override fun onStopTrackingTouch(p0: RangeSlider) {
                val start = p0.values.first()
                val end = p0.values.last()
                val range = "$start-$end"
                viewModel.updateKpRatingRange(range)
            }
        })
    }

    private fun setOnShowButtonClickListener() {
        binding.btnShow.setOnClickListener {
            val type = when (binding.toggleButton.checkedButtonId) {
                R.id.btn_all -> null
                R.id.btn_movies -> "movie"
                R.id.btn_tv_series -> "tv-series"
                else -> null
            }
            viewModel.updateAgeRatingAndType(
                rating = binding.spinnerAgeRating.selectedItem.toString(),
                type = type
            )

            findNavController().navigate(
                R.id.action_fragmentFilters_to_searchMoviesFragment,
                SearchMoviesFragment.createArgs(true)
            )
        }
    }

    private fun setOnResetBtnClickListener() {
        binding.btnReset.setOnClickListener {
            viewModel.clearFilters()
        }
    }
}
