package ru.practicum.android.diploma.search.domain.models

data class Vacancy(
    val id: String,
    val name: String,
    val address: Address?,
    val description: String?,
    val salary: Salary?,
    val employerName: String?,
    val employerLogo: String?,
    val areaName: String?,
    val url: String?
)
