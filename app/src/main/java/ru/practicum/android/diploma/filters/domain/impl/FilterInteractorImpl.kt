package ru.practicum.android.diploma.filters.domain.impl

import ru.practicum.android.diploma.filters.domain.api.FilterInteractor
import ru.practicum.android.diploma.filters.domain.api.FilterRepository
import ru.practicum.android.diploma.filters.domain.models.FiltersParameters

class FilterInteractorImpl(
    private val filterRepository: FilterRepository
) : FilterInteractor {
    override fun getFilters(): FiltersParameters? {
        return filterRepository.getFilters()
    }

    override fun saveFilters(filters: FiltersParameters) {
        filterRepository.saveFilters(filters)
    }

    override fun clearFilters() {
        filterRepository.clearFilters()
    }
}
