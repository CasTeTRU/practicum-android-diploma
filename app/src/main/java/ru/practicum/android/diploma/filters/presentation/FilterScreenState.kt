package ru.practicum.android.diploma.filters.presentation

import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.util.UiError

data class FilterScreenState(
    val salary: Int? = null,
    val onlyWithSalary: Boolean = false,
    val industry: FilterIndustry? = null,
    val error: UiError? = null
)
