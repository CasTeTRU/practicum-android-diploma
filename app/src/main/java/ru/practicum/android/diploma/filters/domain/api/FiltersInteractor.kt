package ru.practicum.android.diploma.filters.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.filters.domain.models.FiltersParameters

interface FiltersInteractor {
    suspend fun loadIndustries(): Flow<Result<List<FilterIndustry>>>
    suspend fun getFilterSettings(): FiltersParameters
    suspend fun saveFilterSettings(settings: FiltersParameters)
    suspend fun resetFilterSettings()
    suspend fun getIndustry(): FilterIndustry?
    suspend fun saveIndustry(industry: FilterIndustry)
    suspend fun clearIndustry()
    suspend fun restorePreviousState()
}
