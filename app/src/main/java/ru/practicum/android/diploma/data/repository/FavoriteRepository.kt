package ru.practicum.android.diploma.data.repository

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
        } catch (e: Exception) {
            throw Exception("Ошибка при получении избранных вакансий", e)
        }
    }

    override suspend fun addToFavorites(vacancy: VacancyDetailDTO) {
        try {
            val entity = vacancy.toVacancyEntity()
            vacancyDao.insertFavorite(entity)
        } catch (e: Exception) {
            throw Exception("Ошибка при добавлении вакансии в избранное", e)
        }
    }

    override suspend fun removeFromFavorites(vacancyId: String) {
        try {
            vacancyDao.deleteFavorite(vacancyId)
        } catch (e: Exception) {
            throw Exception("Ошибка при удалении вакансии из избранного", e)
        }
    }

    override suspend fun isFavorite(vacancyId: String): Boolean {
        return try {
            vacancyDao.isFavorite(vacancyId)
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getVacancyById(vacancyId: String): VacancyDetailDTO? {
        return try {
            val entity = vacancyDao.getFavoriteById(vacancyId)
            entity?.toVacancyDetailDTO()
        } catch (e: Exception) {
            null
        }
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
