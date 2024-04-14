package com.github.lzaytseva.kinopoiskapitest.data.storage

import com.github.lzaytseva.kinopoiskapitest.domain.model.Filters
import javax.inject.Inject

class FiltersStorageImpl @Inject constructor() : FiltersStorage {

    private var currentFilters: Filters = Filters()
    override fun save(filters: Filters) {
        currentFilters = filters
    }

    override fun get(): Filters {
        return currentFilters
    }

    override fun clear() {
        currentFilters = Filters()
    }
}