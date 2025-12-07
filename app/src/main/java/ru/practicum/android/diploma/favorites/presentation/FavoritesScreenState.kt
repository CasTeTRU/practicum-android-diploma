package ru.practicum.android.diploma.favorites.presentation

import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailed

sealed class FavoritesScreenState {
    object Loading : FavoritesScreenState()
    object Empty : FavoritesScreenState()
    data class Content(val favorites: List<VacancyDetailed>) : FavoritesScreenState()
    object Error : FavoritesScreenState()
}
