package com.github.lzaytseva.kinopoiskapitest.data.storage

import com.github.lzaytseva.kinopoiskapitest.domain.model.Filters

interface FiltersStorage {
    fun save(filters: Filters)

    fun get(): Filters

    fun clear()
}