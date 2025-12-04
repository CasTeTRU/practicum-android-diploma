package ru.practicum.android.diploma.vacancy.presentation

import ru.practicum.android.diploma.search.presentation.UiError
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailed

sealed class VacancyScreenState {
    object Loading : VacancyScreenState()
    data class ShowContent(val vacancy: VacancyDetailed) : VacancyScreenState()
    data class Error(val error: UiError) : VacancyScreenState()
}
