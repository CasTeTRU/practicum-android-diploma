package ru.practicum.android.diploma.favorites.domain.impl

import ru.practicum.android.diploma.data.dto.responses.VacancyDetailDTO
import ru.practicum.android.diploma.favorites.domain.api.FavoriteInteractor
import ru.practicum.android.diploma.favorites.domain.api.FavoriteRepository

class FavoriteInteractorImpl(
    private val favoriteRepository: FavoriteRepository
) : FavoriteInteractor {
    override suspend fun getFavorites(): List<VacancyDetailDTO> {
        return favoriteRepository.getFavorites()
    }

    override suspend fun addToFavorites(vacancy: VacancyDetailDTO) {
        favoriteRepository.addToFavorites(vacancy)
    }

    override suspend fun removeFromFavorites(vacancyId: String) {
        favoriteRepository.removeFromFavorites(vacancyId)
    }

    override suspend fun isFavorite(vacancyId: String): Boolean {
        return favoriteRepository.isFavorite(vacancyId)
    }

    override suspend fun getVacancyById(vacancyId: String): VacancyDetailDTO? {
        return favoriteRepository.getVacancyById(vacancyId)
    }
}
