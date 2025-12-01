package ru.practicum.android.diploma.data.repository

import ru.practicum.android.diploma.data.database.VacancyEntity
import ru.practicum.android.diploma.data.dto.responses.VacancyDetailDTO

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
