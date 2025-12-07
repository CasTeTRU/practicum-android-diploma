package ru.practicum.android.diploma.vacancy.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailed

interface VacancyRepository {
    suspend fun getVacancyById(id: String): Flow<Result<VacancyDetailed>>
}
