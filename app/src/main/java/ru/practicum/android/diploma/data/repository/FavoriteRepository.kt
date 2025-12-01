package ru.practicum.android.diploma.data.repository

import ru.practicum.android.diploma.data.database.VacancyDao
import ru.practicum.android.diploma.data.database.VacancyEntity
import ru.practicum.android.diploma.data.dto.responses.VacancyDetailDTO
import ru.practicum.android.diploma.favorites.domain.api.FavoriteRepository as FavoriteRepositoryDomain

class FavoriteRepositoryImpl(
    private val vacancyDao: VacancyDao
) : FavoriteRepositoryDomain {

    override suspend fun getFavorites(): List<VacancyDetailDTO> {
        val entities = vacancyDao.getAllFavoritesSuspend()
        return entities.map { it.toVacancyDetailDTO() }
    }

    override suspend fun addToFavorites(vacancy: VacancyDetailDTO) {
        val entity = vacancy.toVacancyEntity()
        vacancyDao.insertFavorite(entity)
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
