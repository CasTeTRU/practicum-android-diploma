package ru.practicum.android.diploma.search.data.mapper

import ru.practicum.android.diploma.data.dto.responses.VacanciesSearchResponse
import ru.practicum.android.diploma.data.dto.responses.VacancyDetailDTO
import ru.practicum.android.diploma.data.mapper.toDomain
import ru.practicum.android.diploma.search.domain.models.VacanciesPage
import ru.practicum.android.diploma.search.domain.models.Vacancy

fun VacancyDetailDTO.toDomain(): Vacancy = Vacancy(
    id = id,
    name = name,
    address = address?.toDomain(),
    salary = salary?.toDomain(),
    employerName = employer.name,
    employerLogo = employer.logo,
    areaName = area.name,
    url = url
)

fun VacanciesSearchResponse.toDomain(): VacanciesPage = VacanciesPage(
    vacancies = this.items.map { it.toDomain() },
    found = this.found,
    page = this.page,
    pages = this.pages,
)
