package ru.practicum.android.diploma.filters.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.FilterIndustry

interface IndustryRepository {
    suspend fun getIndustries(): Flow<Result<List<FilterIndustry>>>
}
