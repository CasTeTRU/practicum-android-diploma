package ru.practicum.android.diploma.vacancy.presentation

import ru.practicum.android.diploma.util.UiError
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailed

sealed class VacancyScreenState {
    object Loading : VacancyScreenState()
    data class ShowContent(
        val vacancy: VacancyDetailed,
        val isFavorite: Boolean
    ) : VacancyScreenState()
    data class Error(val error: UiError) : VacancyScreenState()
}
