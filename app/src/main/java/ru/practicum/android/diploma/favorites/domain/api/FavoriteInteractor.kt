package ru.practicum.android.diploma.favorites.domain.api

import ru.practicum.android.diploma.data.dto.responses.VacancyDetailDTO

interface FavoriteInteractor {
    suspend fun getFavorites(): List<VacancyDetailDTO>
    suspend fun addToFavorites(vacancy: VacancyDetailDTO)
    suspend fun removeFromFavorites(vacancyId: String)
    suspend fun isFavorite(vacancyId: String): Boolean
    suspend fun getVacancyById(vacancyId: String): VacancyDetailDTO?
}
