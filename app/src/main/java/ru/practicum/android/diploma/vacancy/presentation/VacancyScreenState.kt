package ru.practicum.android.diploma.vacancy.presentation

import ru.practicum.android.diploma.data.dto.responses.VacancyDetailDTO

sealed class VacancyScreenState {
    object Loading : VacancyScreenState()
    data class Content(val vacancy: VacancyDetailDTO) : VacancyScreenState()
    object Error : VacancyScreenState()
}
