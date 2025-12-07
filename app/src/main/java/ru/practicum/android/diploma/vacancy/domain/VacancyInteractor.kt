package ru.practicum.android.diploma.vacancy.domain

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailed

interface VacancyInteractor {
    fun shareLink(alternateUrl: String)
    fun emailTo(email: String)
    fun callTo(phoneNumber: String)
    suspend fun getVacancyById(id: String): Flow<Result<VacancyDetailed>>
}
