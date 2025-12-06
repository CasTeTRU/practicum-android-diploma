package ru.practicum.android.diploma.favorites.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailed

interface FavoriteInteractor {
    suspend fun getFavorites(): Flow<List<VacancyDetailed>>
    suspend fun addToFavorites(vacancy: VacancyDetailed)
    suspend fun removeFromFavorites(vacancyId: String)
    suspend fun isFavorite(vacancyId: String): Boolean
    suspend fun getVacancyById(vacancyId: String): VacancyDetailed?
}
