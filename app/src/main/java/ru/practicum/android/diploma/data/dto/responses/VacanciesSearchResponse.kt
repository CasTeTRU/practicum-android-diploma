package ru.practicum.android.diploma.data.dto.responses

data class VacanciesSearchResponse(
    val found: Int,
    val page: Int,
    val pages: Int,
    val items: List<VacancyDetailDTO>,
)
