package ru.practicum.android.diploma.vacancy.presentation

import android.database.SQLException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.data.ApiError
import ru.practicum.android.diploma.data.ResponceCodes
import ru.practicum.android.diploma.favorites.domain.api.FavoriteInteractor
import ru.practicum.android.diploma.search.presentation.UiError
import ru.practicum.android.diploma.vacancy.domain.VacancyInteractor
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailed

class VacancyViewModel(
    private val vacancyInteractor: VacancyInteractor,
    private val favoriteInteractor: FavoriteInteractor
): ViewModel()  {
    private var lastState: VacancyScreenState? = null
    private var _vacancyStatusLiveData = MutableLiveData<VacancyScreenState>()
    val vacancyStatusLiveData: LiveData<VacancyScreenState> = _vacancyStatusLiveData

    fun searchVacancyById(id: String) {
        viewModelScope.launch {
            vacancyInteractor
                .getVacancyById(id)
                .collect { result ->
                    result.fold(
                        onSuccess = { vacancy -> handleSuccess(vacancy) },
                        onFailure = { throwable -> handleError(throwable) }
                    )
            }
        }
    }

    private fun handleSuccess(vacancy: VacancyDetailed) {
        renderState(
            VacancyScreenState.ShowContent(
                vacancy = vacancy,
            )
        )
    }

    private fun handleError(throwable: Throwable) {

        val uiError: UiError = when (throwable) {
            is java.net.UnknownHostException -> UiError.NoInternet
            is ApiError -> when (throwable.code) {
                ResponceCodes.ERROR_NO_INTERNET -> UiError.NoInternet
                ResponceCodes.NOTHING_FOUND -> UiError.NothingFound
                ResponceCodes.IO_EXCEPTION,
                ResponceCodes.MAPPER_EXCEPTION -> UiError.ServerError
                else -> UiError.Unknown(throwable.code)
            }

            else -> UiError.Unknown(ResponceCodes.IO_EXCEPTION)
        }

        renderState(VacancyScreenState.Error(uiError))
    }

    private fun renderState(state: VacancyScreenState) {
        lastState = state
        _vacancyStatusLiveData.postValue(state)
    }

//    fun checkFavoriteStatus(vacancyId: String) {
//        viewModelScope.launch {
//            runCatching {
//                favoriteInteractor.isFavorite(vacancyId)
//            }.onSuccess { isFavorite ->
//                _isFavoriteState.value = isFavorite
//            }.onFailure {
//                _isFavoriteState.value = false
//            }
//        }
//    }
//
//   fun addToFavorites(vacancy: VacancyDetailDTO) {
//        viewModelScope.launch {
//            runCatching {
//                favoriteInteractor.addToFavorites(vacancy)
//            }.onSuccess {
//                _isFavoriteState.value = true
//            }.onFailure {
//                // Ошибка при добавлении в избранное - состояние не меняем
//            }
//        }
//    }

//    fun removeFromFavorites(vacancyId: String) {
//        viewModelScope.launch {
//            runCatching {
//                favoriteInteractor.removeFromFavorites(vacancyId)
//            }.onSuccess {
//                _isFavoriteState.value = false
//            }.onFailure {
//                // Ошибка при удалении из избранного - состояние не меняем
//            }
//        }
//    }

//    fun getVacancyFromFavorites(vacancyId: String) {
//        viewModelScope.launch {
//            _vacancyState.value = VacancyScreenState.Loading
//            val vacancy = favoriteInteractor.getVacancyById(vacancyId)
//            if (vacancy != null) {
//                _vacancyState.value = VacancyScreenState.Content(vacancy)
//                _isFavoriteState.value = true
//            } else {
//                _vacancyState.value = VacancyScreenState.Error
//            }
//        }
//    }
}
