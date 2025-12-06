package ru.practicum.android.diploma.favorites.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.favorites.domain.api.FavoriteInteractor
import ru.practicum.android.diploma.favorites.domain.api.FavoriteRepository
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailed

class FavoriteInteractorImpl(
    private val favoriteRepository: FavoriteRepository
) : FavoriteInteractor {
    override suspend fun getFavorites(): Flow<List<VacancyDetailed>> {
        return favoriteRepository.getFavorites()
    }
    override suspend fun addToFavorites(vacancy: VacancyDetailed) {
        favoriteRepository.addToFavorites(vacancy)
    }
    override suspend fun removeFromFavorites(vacancyId: String) {
        favoriteRepository.removeFromFavorites(vacancyId)
    }
    override suspend fun isFavorite(vacancyId: String): Boolean {
        return favoriteRepository.isFavorite(vacancyId)
    }
    override suspend fun getVacancyById(vacancyId: String): VacancyDetailed? {
        return favoriteRepository.getVacancyById(vacancyId)
    }
}
