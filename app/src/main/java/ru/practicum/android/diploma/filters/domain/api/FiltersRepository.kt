package ru.practicum.android.diploma.filters.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.filters.domain.models.FiltersParameters

interface FiltersRepository {
    suspend fun fetchIndustries(): Flow<Result<List<FilterIndustry>>>
    suspend fun getSavedFilter(): FiltersParameters
    suspend fun saveFilter(settings: FiltersParameters)
    suspend fun clearFilter()
    suspend fun getIndustry(): FilterIndustry?
    suspend fun saveIndustry(industry: FilterIndustry)
    suspend fun clearIndustry()
    suspend fun restorePreviousState()
}
