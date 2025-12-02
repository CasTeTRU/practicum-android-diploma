package ru.practicum.android.diploma.vacancy.presentation

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
//            try {
//                val isFavorite = favoriteInteractor.isFavorite(vacancyId)
//                _isFavoriteState.value = isFavorite
//            } catch (e: SQLiteException) {
//                _isFavoriteState.value = false
//            } catch (e: IOException) {
//                _isFavoriteState.value = false
//            } catch (e: RuntimeException) {
//                _isFavoriteState.value = false
//            }
//        }
//    }
//
//    fun addToFavorites(vacancy: VacancyDetailDTO) {
//        viewModelScope.launch {
//            try {
//                favoriteInteractor.addToFavorites(vacancy)
//                _isFavoriteState.value = true
//            } catch (e: SQLiteException) {
//            } catch (e: IOException) {
//            } catch (e: RuntimeException) {
//            }
//        }
//    }

//    fun removeFromFavorites(vacancyId: String) {
//        viewModelScope.launch {
//            try {
//                favoriteInteractor.removeFromFavorites(vacancyId)
//                _isFavoriteState.value = false
//            } catch (e: SQLiteException) {
//                // ignore
//            } catch (e: IOException) {
//                // ignore
//            } catch (e: RuntimeException) {
//                // ignore
//            }
//        }
//    }

//    fun getVacancyFromFavorites(vacancyId: String) {
//        viewModelScope.launch {
//            _vacancyState.value = VacancyScreenState.Loading
//            try {
//                val vacancy = favoriteInteractor.getVacancyById(vacancyId)
//                if (vacancy != null) {
//                    _vacancyState.value = VacancyScreenState.Content(vacancy)
//                    _isFavoriteState.value = true
//                } else {
//                    _vacancyState.value = VacancyScreenState.Error
//                }
//            } catch (e: SQLiteException) {
//                _vacancyState.value = VacancyScreenState.Error
//            } catch (e: IOException) {
//                _vacancyState.value = VacancyScreenState.Error
//            } catch (e: RuntimeException) {
//                _vacancyState.value = VacancyScreenState.Error
//            }
//        }
//    }
}
