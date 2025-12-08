package ru.practicum.android.diploma.filters.presentation

import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.util.UiError

data class IndustryScreenState(
    val query: String = "",
    val industryList: List<FilterIndustry> = emptyList(),
    val selected: FilterIndustry? = null,
    val isLoading: Boolean = false,
    val error: UiError? = null
)
