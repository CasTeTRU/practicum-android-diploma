package ru.practicum.android.diploma.filters.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.filters.domain.api.IndustryInteractor
import ru.practicum.android.diploma.filters.domain.api.IndustryRepository

class IndustryInteractorImpl(
    private val repository: IndustryRepository
) : IndustryInteractor {
    override suspend fun getIndustries(): Flow<Result<List<FilterIndustry>>> {
        return repository.getIndustries()
    }
}
