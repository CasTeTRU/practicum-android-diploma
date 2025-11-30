package ru.practicum.android.diploma.vacancy.domain

import ru.practicum.android.diploma.vacancy.domain.models.Vacancy

interface VacancyRepository {

    suspend fun getVacancyById(id: String): Result<Vacancy>
}
