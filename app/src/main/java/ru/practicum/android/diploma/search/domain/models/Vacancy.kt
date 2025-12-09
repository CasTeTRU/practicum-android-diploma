package ru.practicum.android.diploma.search.domain.models

import ru.practicum.android.diploma.domain.models.Address
import ru.practicum.android.diploma.domain.models.Salary

data class Vacancy(
    val id: String,
    val name: String,
    val address: Address?,
    val salary: Salary?,
    val employerName: String?,
    val employerLogo: String?,
    val areaName: String?,
    val url: String?
)
