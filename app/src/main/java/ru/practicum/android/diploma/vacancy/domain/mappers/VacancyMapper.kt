package ru.practicum.android.diploma.vacancy.domain.mappers

import ru.practicum.android.diploma.data.dto.responses.VacancyByIdResponse
import ru.practicum.android.diploma.data.mapper.toDomain
import ru.practicum.android.diploma.domain.models.KeySkill
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailed

fun VacancyByIdResponse.toDomain(): VacancyDetailed = VacancyDetailed(
    address = address?.toDomain(),
    url = url,
    area = area.toDomain(),
    contacts = contacts?.toDomain(),
    description = description,
    employer = employer.toDomain(),
    employment = employment?.toDomain(),
    experience = experience?.toDomain(),
    id,
    keySkills = skills.map { KeySkill(it) },
    name,
    salary = salary?.toDomain(),
    schedule = schedule?.toDomain(),
)

