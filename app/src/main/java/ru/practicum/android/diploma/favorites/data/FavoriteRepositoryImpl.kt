package ru.practicum.android.diploma.favorites.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.database.VacancyDao
import ru.practicum.android.diploma.favorites.data.mapper.toVacancyDetailed
import ru.practicum.android.diploma.favorites.data.mapper.toVacancyEntity
import ru.practicum.android.diploma.favorites.domain.api.FavoriteRepository
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailed

class FavoriteRepositoryImpl(
    private val vacancyDao: VacancyDao
) : FavoriteRepository {

    override suspend fun getFavorites(): Flow<List<VacancyDetailed>> {
        return vacancyDao.getAllFavorites()
            .map { list -> list.map { it.toVacancyDetailed() } }
    }

    override suspend fun addToFavorites(vacancy: VacancyDetailed) {
        vacancyDao.insertFavorite(vacancy.toVacancyEntity())
    }

    override suspend fun removeFromFavorites(vacancyId: String) {
        vacancyDao.deleteFavorite(vacancyId)
    }

    override suspend fun isFavorite(vacancyId: String): Boolean {
        return vacancyDao.isFavorite(vacancyId)
    }

    override suspend fun getVacancyById(vacancyId: String): VacancyDetailed? {
        return vacancyDao.getFavoriteById(vacancyId)?.toVacancyDetailed()
    }
}
