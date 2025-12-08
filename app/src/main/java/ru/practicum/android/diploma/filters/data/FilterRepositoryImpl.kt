package ru.practicum.android.diploma.filters.data

import ru.practicum.android.diploma.filters.domain.api.FilterRepository
import ru.practicum.android.diploma.filters.domain.models.FiltersParameters

class FilterRepositoryImpl(
    private val filterStorage: FilterStorage
) : FilterRepository {
    override fun getFilters(): FiltersParameters? {
        return filterStorage.getFilters()
    }

    override fun saveFilters(filters: FiltersParameters) {
        filterStorage.saveFilters(filters)
    }

    override fun clearFilters() {
        filterStorage.clearFilters()
    }
}
