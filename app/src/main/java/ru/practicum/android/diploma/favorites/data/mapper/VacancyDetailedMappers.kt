package ru.practicum.android.diploma.favorites.data.mapper

import ru.practicum.android.diploma.data.database.VacancyEntity
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailed

fun VacancyDetailed.toVacancyEntity(): VacancyEntity {
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
        skills = keySkills,
        url = url,
        industry = industry
    )
}

fun VacancyEntity.toVacancyDetailed(): VacancyDetailed {
    return VacancyDetailed(
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
        keySkills = skills,
        url = url,
        industry = industry
    )
}
