package ru.practicum.android.diploma.vacancy.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.data.dto.responses.VacancyDetailDTO
import ru.practicum.android.diploma.favorites.domain.api.FavoriteInteractor
import android.database.sqlite.SQLiteException
import java.io.IOException

class VacancyViewModel(
    private val favoriteInteractor: FavoriteInteractor
) : ViewModel() {

    private val _isFavoriteState = MutableLiveData<Boolean>()
    val isFavoriteState: LiveData<Boolean> = _isFavoriteState

    private val _vacancyState = MutableLiveData<VacancyScreenState>()
    val vacancyState: LiveData<VacancyScreenState> = _vacancyState
/*    fun checkFavoriteStatus(vacancyId: String) {
        viewModelScope.launch {
            try {
                val isFavorite = favoriteInteractor.isFavorite(vacancyId)
                _isFavoriteState.value = isFavorite
            } catch (e: SQLiteException) {
                _isFavoriteState.value = false
            } catch (e: IOException) {
                _isFavoriteState.value = false
            } catch (e: RuntimeException) {
                _isFavoriteState.value = false
            }
        }
    }

    fun addToFavorites(vacancy: VacancyDetailDTO) {
        viewModelScope.launch {
            try {
                favoriteInteractor.addToFavorites(vacancy)
                _isFavoriteState.value = true
            } catch (e: SQLiteException) {
            } catch (e: IOException) {
            } catch (e: RuntimeException) {
            }
        }
    }

    fun removeFromFavorites(vacancyId: String) {
        viewModelScope.launch {
            try {
                favoriteInteractor.removeFromFavorites(vacancyId)
                _isFavoriteState.value = false
            } catch (e: SQLiteException) {
                // ignore
            } catch (e: IOException) {
                // ignore
            } catch (e: RuntimeException) {
                // ignore
            }
        }
    }

    fun getVacancyFromFavorites(vacancyId: String) {
        viewModelScope.launch {
            _vacancyState.value = VacancyScreenState.Loading
            try {
                val vacancy = favoriteInteractor.getVacancyById(vacancyId)
                if (vacancy != null) {
                    _vacancyState.value = VacancyScreenState.Content(vacancy)
                    _isFavoriteState.value = true
                } else {
                    _vacancyState.value = VacancyScreenState.Error
                }
            } catch (e: SQLiteException) {
                _vacancyState.value = VacancyScreenState.Error
            } catch (e: IOException) {
                _vacancyState.value = VacancyScreenState.Error
            } catch (e: RuntimeException) {
                _vacancyState.value = VacancyScreenState.Error
            }
        }
    }
}
*/
