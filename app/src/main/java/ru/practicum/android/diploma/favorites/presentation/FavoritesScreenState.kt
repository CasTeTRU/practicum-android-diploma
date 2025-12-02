package ru.practicum.android.diploma.favorites.presentation

import ru.practicum.android.diploma.data.dto.responses.VacancyDetailDTO

sealed class FavoritesScreenState {
    object Loading : FavoritesScreenState()
    object Empty : FavoritesScreenState()
    data class Content(val favorites: List<VacancyDetailDTO>) : FavoritesScreenState()
    object Error : FavoritesScreenState()
}
