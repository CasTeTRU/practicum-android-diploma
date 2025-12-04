package ru.practicum.android.diploma.domain.favorite

import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailed

interface FavoriteInteractor {
    suspend fun toggleFavorite(vacancy: VacancyDetailed)
}
