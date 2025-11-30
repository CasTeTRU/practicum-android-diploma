package ru.practicum.android.diploma.vacancy.domain

import ru.practicum.android.diploma.vacancy.domain.models.VacancyShow

interface VacancyInteractor {

    suspend fun getVacancyById(id: String): Result<VacancyShow>
}
