package ru.practicum.android.diploma.vacancy.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.data.ResponceCodes
import ru.practicum.android.diploma.data.dto.responses.VacancyDetailDTO
import ru.practicum.android.diploma.data.dto.responses.VacancyDetailResponse
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.favorites.domain.api.FavoriteInteractor

class VacancyViewModel(
    private val favoriteInteractor: FavoriteInteractor,
    private val networkClient: NetworkClient
) : ViewModel() {
    private val _isFavoriteState = MutableLiveData<Boolean>()
    val isFavoriteState: LiveData<Boolean> = _isFavoriteState

    private val _vacancyState = MutableLiveData<VacancyScreenState>()
    val vacancyState: LiveData<VacancyScreenState> = _vacancyState

    fun checkFavoriteStatus(vacancyId: String) {
        viewModelScope.launch {
            runCatching {
                favoriteInteractor.isFavorite(vacancyId)
            }.onSuccess { isFavorite ->
                _isFavoriteState.value = isFavorite
            }.onFailure {
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
            }.onFailure {
                // Ошибка при добавлении в избранное - состояние не меняем
            }
        }
    }

    fun removeFromFavorites(vacancyId: String) {
        viewModelScope.launch {
            runCatching {
                favoriteInteractor.removeFromFavorites(vacancyId)
            }.onSuccess {
                _isFavoriteState.value = false
            }.onFailure {
                // Ошибка при удалении из избранного - состояние не меняем
            }
        }
    }

    fun loadVacancy(vacancyId: String) {
        viewModelScope.launch {
            _vacancyState.value = VacancyScreenState.Loading
            checkFavoriteStatus(vacancyId)
            // Сначала пытаемся загрузить из избранного (офлайн)
            runCatching {
                favoriteInteractor.getVacancyById(vacancyId)
            }.onSuccess { vacancy ->
                if (vacancy != null) {
                    _vacancyState.value = VacancyScreenState.Content(vacancy)
                    _isFavoriteState.value = true
                } else {
                    // Если не найдено в избранном, загружаем из API (онлайн)
                    loadVacancyFromApi(vacancyId)
                }
            }.onFailure {
                // Если ошибка при загрузке из избранного, пытаемся загрузить из API
                loadVacancyFromApi(vacancyId)
            }
        }
    }

    private suspend fun loadVacancyFromApi(vacancyId: String) {
        runCatching {
            networkClient.getVacancyById(vacancyId)
        }.onSuccess { response ->
            when (response.resultCode) {
                ResponceCodes.SUCCESS_STATUS -> {
                    val vacancyResponse = response as? VacancyDetailResponse
                    val vacancy = vacancyResponse?.vacancy
                    if (vacancy != null) {
                        _vacancyState.value = VacancyScreenState.Content(vacancy)
                        // Проверяем статус избранного после загрузки
                        checkFavoriteStatus(vacancyId)
                    } else {
                        _vacancyState.value = VacancyScreenState.Error
                    }
                }
                ResponceCodes.ERROR_NO_INTERNET -> {
                    // Если нет интернета и вакансия не в избранном, показываем ошибку
                    _vacancyState.value = VacancyScreenState.Error
                }
                else -> {
                    _vacancyState.value = VacancyScreenState.Error
                }
            }
        }.onFailure {
            _vacancyState.value = VacancyScreenState.Error
        }
    }
}
