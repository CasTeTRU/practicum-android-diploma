package ru.practicum.android.diploma.favorites.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.favorites.domain.api.FavoriteInteractor

class FavoritesViewModel(
    private val favoriteInteractor: FavoriteInteractor
) : ViewModel() {

    private val _favoritesState = MutableLiveData<FavoritesScreenState>()
    val favoritesState: LiveData<FavoritesScreenState> = _favoritesState

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            try {
                _favoritesState.value = FavoritesScreenState.Loading
                val favorites = favoriteInteractor.getFavorites()
                if (favorites.isEmpty()) {
                    _favoritesState.value = FavoritesScreenState.Empty
                } else {
                    _favoritesState.value = FavoritesScreenState.Content(favorites)
                }
            } catch (e: Exception) {
                _favoritesState.value = FavoritesScreenState.Error
            }
        }
    }

    fun removeFromFavorites(vacancyId: String) {
        viewModelScope.launch {
            try {
                favoriteInteractor.removeFromFavorites(vacancyId)
                loadFavorites()
            } catch (e: Exception) {
                _favoritesState.value = FavoritesScreenState.Error
            }
        }
    }
}
