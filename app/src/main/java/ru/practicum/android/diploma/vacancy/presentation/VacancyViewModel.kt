package ru.practicum.android.diploma.vacancy.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.favorites.domain.api.FavoriteInteractor

class VacancyViewModel(
    private val favoriteInteractor: FavoriteInteractor
) : ViewModel() {
    private val _isFavoriteState = MutableLiveData<Boolean>()
    val isFavoriteState: LiveData<Boolean> = _isFavoriteState

    private val _vacancyState = MutableLiveData<VacancyScreenState>()
    val vacancyState: LiveData<VacancyScreenState> = _vacancyState

/*    fun checkFavoriteStatus(vacancyId: String) {
        viewModelScope.launch {
            runCatching {
                favoriteInteractor.isFavorite(vacancyId)
            }.onSuccess { isFavorite ->
                _isFavoriteState.value = isFavorite
            }.onFailure { error ->
                handleDatabaseError(error)
                _isFavoriteState.value = false
            }
        }
    }

    fun addToFavorites(vacancy: VacancyDetailDTO) {
        viewModelScope.launch {
            runCatching {
                favoriteInteractor.addToFavorites(vacancy)
            }.onSuccess {
                _isFavoriteState.value = true
            }.onFailure { error ->
                handleDatabaseError(error)
            }
        }
    }

    fun removeFromFavorites(vacancyId: String) {
        viewModelScope.launch {
            runCatching {
                favoriteInteractor.removeFromFavorites(vacancyId)
            }.onSuccess {
                _isFavoriteState.value = false
            }.onFailure { error ->
                handleDatabaseError(error)
            }
        }
    }

    fun getVacancyFromFavorites(vacancyId: String) {
        viewModelScope.launch {
            _vacancyState.value = VacancyScreenState.Loading

            runCatching {
                favoriteInteractor.getVacancyById(vacancyId)
            }.onSuccess { vacancy ->
                if (vacancy != null) {
                    _vacancyState.value = VacancyScreenState.Content(vacancy)
                    _isFavoriteState.value = true
                } else {
                    _vacancyState.value = VacancyScreenState.Error
                }
            }.onFailure { error ->
                handleDatabaseError(error)
                _vacancyState.value = VacancyScreenState.Error
            }
        }
    }

    private fun handleDatabaseError(error: Throwable) {
        if (error is SQLException) {
        }
    }

 */
}
