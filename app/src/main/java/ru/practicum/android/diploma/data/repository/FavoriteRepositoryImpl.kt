package ru.practicum.android.diploma.data.repository

import ru.practicum.android.diploma.data.database.VacancyDao
import ru.practicum.android.diploma.data.database.VacancyEntity
import ru.practicum.android.diploma.data.dto.responses.VacancyDetailDTO
import ru.practicum.android.diploma.favorites.domain.api.FavoriteRepository as FavoriteRepositoryDomain

class FavoriteRepositoryImpl(
    private val vacancyDao: VacancyDao
) : FavoriteRepositoryDomain {

    override suspend fun getFavorites(): List<VacancyDetailDTO> {
        return vacancyDao.getAllFavoritesSuspend()
            .map { it.toVacancyDetailDTO() }
    }

    override suspend fun addToFavorites(vacancy: VacancyDetailDTO) {
        vacancyDao.insertFavorite(vacancy.toVacancyEntity())
    }

    override suspend fun removeFromFavorites(vacancyId: String) {
        vacancyDao.deleteFavorite(vacancyId)
    }

    override suspend fun isFavorite(vacancyId: String): Boolean {
        return vacancyDao.isFavorite(vacancyId)
    }

    override suspend fun getVacancyById(vacancyId: String): VacancyDetailDTO? {
        return vacancyDao.getFavoriteById(vacancyId)?.toVacancyDetailDTO()
    }
}

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


