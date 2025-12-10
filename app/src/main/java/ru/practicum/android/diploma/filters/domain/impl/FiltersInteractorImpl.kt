package ru.practicum.android.diploma.filters.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.filters.domain.api.FiltersInteractor
import ru.practicum.android.diploma.filters.domain.api.FiltersRepository
import ru.practicum.android.diploma.filters.domain.models.FiltersParameters

class FiltersInteractorImpl(
    private val filtersRepository: FiltersRepository
) : FiltersInteractor {
    override suspend fun loadIndustries(): Flow<Result<List<FilterIndustry>>> {
        return filtersRepository.fetchIndustries()
    }

    override suspend fun getFilterSettings(): FiltersParameters {
        return filtersRepository.getSavedFilter()
    }

    override suspend fun saveFilterSettings(settings: FiltersParameters) {
        filtersRepository.saveFilter(settings)
    }

    override suspend fun resetFilterSettings() {
        filtersRepository.clearFilter()
    }

    override suspend fun getIndustry(): FilterIndustry? {
        return filtersRepository.getIndustry()
    }

    override suspend fun saveIndustry(industry: FilterIndustry) {
        filtersRepository.saveIndustry(industry)
    }

    override suspend fun clearIndustry() {
        filtersRepository.clearIndustry()
    }

    override suspend fun restorePreviousState() {
        filtersRepository.restorePreviousState()
    }
}
