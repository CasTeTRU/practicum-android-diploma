package ru.practicum.android.diploma.data.repository

import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.room.RoomDatabaseException
import ru.practicum.android.diploma.data.database.VacancyDao
import ru.practicum.android.diploma.data.database.VacancyEntity
import ru.practicum.android.diploma.data.dto.responses.VacancyDetailDTO
import ru.practicum.android.diploma.favorites.domain.api.FavoriteRepository as FavoriteRepositoryDomain

class FavoriteRepositoryImpl(
    private val vacancyDao: VacancyDao
) : FavoriteRepositoryDomain {

    override suspend fun getFavorites(): List<VacancyDetailDTO> {
        return try {
            val entities = vacancyDao.getAllFavoritesSuspend()
            entities.map { entity ->
                entity.toVacancyDetailDTO()
            }
        } catch (e: SQLiteException) {
            throw RoomDatabaseException("Ошибка при получении избранных вакансий", e)
        } catch (e: RoomDatabaseException) {
            throw RoomDatabaseException("Ошибка при получении избранных вакансий", e)
        }
    }

    override suspend fun addToFavorites(vacancy: VacancyDetailDTO) {
        try {
            val entity = vacancy.toVacancyEntity()
            vacancyDao.insertFavorite(entity)
        } catch (e: SQLiteException) {
            throw RoomDatabaseException("Ошибка при добавлении вакансии в избранное", e)
        } catch (e: RoomDatabaseException) {
            throw RoomDatabaseException("Ошибка при добавлении вакансии в избранное", e)
        }
    }

    override suspend fun removeFromFavorites(vacancyId: String) {
        try {
            vacancyDao.deleteFavorite(vacancyId)
        } catch (e: SQLiteException) {
            throw RoomDatabaseException("Ошибка при удалении вакансии из избранного", e)
        } catch (e: RoomDatabaseException) {
            throw RoomDatabaseException("Ошибка при удалении вакансии из избранного", e)
        }
    }

    override suspend fun isFavorite(vacancyId: String): Boolean {
        return try {
            vacancyDao.isFavorite(vacancyId)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Ошибка при проверке избранной вакансии", e)
            false
        } catch (e: RoomDatabaseException) {
            Log.e(TAG, "Ошибка при проверке избранной вакансии", e)
            false
        }
    }

    override suspend fun getVacancyById(vacancyId: String): VacancyDetailDTO? {
        return try {
            val entity = vacancyDao.getFavoriteById(vacancyId)
            entity?.toVacancyDetailDTO()
        } catch (e: SQLiteException) {
            Log.e(TAG, "Ошибка при получении вакансии по ID", e)
            null
        } catch (e: RoomDatabaseException) {
            Log.e(TAG, "Ошибка при получении вакансии по ID", e)
            null
        }
    }

    companion object {
        private const val TAG = "FavoriteRepositoryImpl"
    }
}

private fun VacancyDetailDTO.toVacancyEntity(): VacancyEntity {
    return VacancyEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        salary = this.salary,
        address = this.address,
        experience = this.experience,
        schedule = this.schedule,
        employment = this.employment,
        contacts = this.contacts,
        employer = this.employer,
        area = this.area,
        skills = this.skills,
        url = this.url,
        industry = this.industry
    )
}

private fun VacancyEntity.toVacancyDetailDTO(): VacancyDetailDTO {
    return VacancyDetailDTO(
        id = this.id,
        name = this.name,
        description = this.description,
        salary = this.salary,
        address = this.address,
        experience = this.experience,
        schedule = this.schedule,
        employment = this.employment,
        contacts = this.contacts,
        employer = this.employer,
        area = this.area,
        skills = this.skills,
        url = this.url,
        industry = this.industry
    )
}

