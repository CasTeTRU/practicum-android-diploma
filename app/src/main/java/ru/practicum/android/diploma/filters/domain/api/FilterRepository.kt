package ru.practicum.android.diploma.filters.domain.api

import ru.practicum.android.diploma.filters.domain.models.FiltersParameters

interface FilterRepository {
    fun getFilters(): FiltersParameters?
    fun saveFilters(filters: FiltersParameters)
    fun clearFilters()
}
