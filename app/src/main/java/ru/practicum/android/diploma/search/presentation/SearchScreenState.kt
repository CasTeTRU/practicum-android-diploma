package ru.practicum.android.diploma.search.presentation

import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.util.UiError

data class SearchScreenState(
    val query: String = "",
    val vacancies: List<Vacancy> = emptyList(),
    val found: Int = 0,
    val isLoading: Boolean = false, // первая / основная загрузка (центр. прогресс)
    val isFetching: Boolean = false, // дозагрузка следующих страниц (spinner)
    val error: UiError? = null,
    val page: Int = 1,
    val canLoadMore: Boolean = true
)
