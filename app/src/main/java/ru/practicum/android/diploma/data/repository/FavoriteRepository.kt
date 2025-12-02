package ru.practicum.android.diploma.data.repository

import android.database.sqlite.SQLiteException
import android.util.Log
import ru.practicum.android.diploma.data.database.VacancyDao
import ru.practicum.android.diploma.data.database.VacancyEntity
import ru.practicum.android.diploma.data.dto.responses.VacancyDetailDTO
import java.io.IOException
import ru.practicum.android.diploma.favorites.domain.api.FavoriteRepository as FavoriteRepositoryDomain

class FavoriteRepositoryImpl(
    private val vacancyDao: VacancyDao
) : FavoriteRepositoryDomain {

    override suspend fun getFavorites(): List<VacancyDetailDTO> {
        return try {
            vacancyDao.getAllFavoritesSuspend()
                .map { it.toVacancyDetailDTO() }
        } catch (e: SQLiteException) {
            throw DataAccessException("Database error while fetching favorites", e)
        } catch (e: IOException) {
            throw DataAccessException("IO error while fetching favorites", e)
        }
    }

    override suspend fun addToFavorites(vacancy: VacancyDetailDTO) {
        try {
            vacancyDao.insertFavorite(vacancy.toVacancyEntity())
        } catch (e: SQLiteException) {
            throw DataAccessException("Database error while adding to favorites", e)
        } catch (e: IOException) {
            throw DataAccessException("IO error while adding to favorites", e)
        }
    }

    override suspend fun removeFromFavorites(vacancyId: String) {
        try {
            vacancyDao.deleteFavorite(vacancyId)
        } catch (e: SQLiteException) {
            throw DataAccessException("Database error while removing from favorites", e)
        } catch (e: IOException) {
            throw DataAccessException("IO error while removing from favorites", e)
        }
    }

    override suspend fun isFavorite(vacancyId: String): Boolean {
        return try {
            vacancyDao.isFavorite(vacancyId)
        } catch (ignoredException: SQLiteException) {
            Log.e("FavoriteRepository", "Database error while checking favorite status", ignoredException)
            false
        } catch (ignoredException: IOException) {
            Log.e("FavoriteRepository", "IO error while checking favorite status", ignoredException)
            false
        }
    }

    override suspend fun getVacancyById(vacancyId: String): VacancyDetailDTO? {
        return try {
            vacancyDao.getFavoriteById(vacancyId)?.toVacancyDetailDTO()
        } catch (ignoredException: SQLiteException) {
            Log.e("FavoriteRepository", "Database error while fetching vacancy by id", ignoredException)
            null
        } catch (ignoredException: IOException) {
            Log.e("FavoriteRepository", "IO error while fetching vacancy by id", ignoredException)
            null
        } catch (ignoredException: Exception) {
            Log.e("FavoriteRepository", "Unexpected error while fetching vacancy by id", ignoredException)
            null
        }
    }
}

class DataAccessException(message: String, cause: Throwable) : RuntimeException(message, cause)

private fun VacancyDetailDTO.toVacancyEntity(): VacancyEntity {
    return VacancyEntity(
        id = id,
        name = name,
        description = description,
        salary = salary,
        address = address,
        experience = experience,
        schedule = schedule,
        employment = employment,
        contacts = contacts,
        employer = employer,
        area = area,
        skills = skills,
        url = url,
        industry = industry
    )
}

private fun VacancyEntity.toVacancyDetailDTO(): VacancyDetailDTO {
    return VacancyDetailDTO(
        id = id,
        name = name,
        description = description,
        salary = salary,
        address = address,
        experience = experience,
        schedule = schedule,
        employment = employment,
        contacts = contacts,
        employer = employer,
        area = area,
        skills = skills,
        url = url,
        industry = industry
    )
}
