package ru.practicum.android.diploma.filters.domain.models

import ru.practicum.android.diploma.domain.models.FilterIndustry

data class FiltersParameters(
    val salary: Int?,
    val onlyWithSalary: Boolean = false,
    val industry: FilterIndustry? = null,
    val area: String? = null
)
