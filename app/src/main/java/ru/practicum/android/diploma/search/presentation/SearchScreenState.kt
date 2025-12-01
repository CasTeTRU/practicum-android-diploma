package ru.practicum.android.diploma.search.presentation

import ru.practicum.android.diploma.data.dto.responses.VacancyDetailDTO

sealed class SearchScreenState {
    data object Default : SearchScreenState()
    data object Loading : SearchScreenState()
    data class ShowContent(val vacancies: ArrayList<VacancyDetailDTO>, val found: Int) : SearchScreenState()
    data class Error(val errorMessage: String) : SearchScreenState()
}
