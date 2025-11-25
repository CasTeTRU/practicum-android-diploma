package ru.practicum.android.diploma.data.dto.requests

data class VacanciesSearchRequest(
    val query: String,
    val area: String? = null,
    val salary: Int? = null,
    val industry: Int? = null,
    val page: Int = 0,
    val onlyWithSalary: Boolean = false
)
