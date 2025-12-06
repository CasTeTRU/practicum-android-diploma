package ru.practicum.android.diploma.vacancy.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.domain.util.ResourcesProviderInteractor
import ru.practicum.android.diploma.favorites.domain.api.FavoriteInteractor
import ru.practicum.android.diploma.util.SingleLiveEvent
import ru.practicum.android.diploma.util.UiError
import ru.practicum.android.diploma.util.UiEvent
import ru.practicum.android.diploma.vacancy.domain.VacancyInteractor
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailed

class VacancyViewModel(
    private val vacancyInteractor: VacancyInteractor,
    private val favoriteInteractor: FavoriteInteractor,
    private val resourcesProvider: ResourcesProviderInteractor
): ViewModel()  {
    private var lastVacancy: VacancyDetailed? = null
    private var isFavorite: Boolean = false
    private var _vacancyStatusLiveData = MutableLiveData<VacancyScreenState>()
    val vacancyStatusLiveData: LiveData<VacancyScreenState> = _vacancyStatusLiveData
    private val _events = SingleLiveEvent<UiEvent>()
    val events: LiveData<UiEvent> = _events

    fun loadVacancy(id: String) {
        renderState(VacancyScreenState.Loading)

        viewModelScope.launch {
            val localFavorite = favoriteInteractor.getVacancyById(id)

            if (localFavorite != null) {
                lastVacancy = localFavorite
                isFavorite = true
                renderState(VacancyScreenState.ShowContent(localFavorite, true))
            } else {
                vacancyInteractor
                    .getVacancyById(id)
                    .collect { result ->
                        result.fold(
                            onSuccess = { vacancy ->
                                lastVacancy = vacancy
                                loadFavoriteStatusAndEmit(vacancy)
                            },
                            onFailure = { throwable ->
                                renderState(VacancyScreenState.Error(UiError.NothingFound))
                            }
                        )
                    }
            }
        }
    }

    fun shareLink() {
        lastVacancy?.let {
            vacancyInteractor.shareLink(it.url)
        }
    }

    fun emailTo(email: String) {
        vacancyInteractor.emailTo(email)
    }

    fun callTo(phoneNumber: String) {
        vacancyInteractor.callTo(phoneNumber)
    }

    private suspend fun loadFavoriteStatusAndEmit(vacancy: VacancyDetailed) {
        isFavorite = favoriteInteractor.isFavorite(vacancy.id)
        renderState(VacancyScreenState.ShowContent(
            vacancy = vacancy,
            isFavorite = isFavorite
        ))
    }

    fun toggleFavorite() {
        val vacancy = lastVacancy ?: return
        val newFav = !isFavorite

        isFavorite = newFav
        renderState(VacancyScreenState.ShowContent(vacancy, newFav))
        viewModelScope.launch {
            runCatching {
                if (newFav) {
                    favoriteInteractor.addToFavorites(vacancy)
                } else {
                    favoriteInteractor.removeFromFavorites(vacancy.id)
                }
            }.onSuccess {
                _events.value = UiEvent.ShowMessage(
                    if (newFav) resourcesProvider.getString(R.string.added_to_favorites)
                    else resourcesProvider.getString(R.string.removed_from_favorites)
                )
            }.onFailure { throwable: Throwable ->
                // откат
                isFavorite = !newFav
                renderState(VacancyScreenState.ShowContent(vacancy, !newFav))

                _events.value = UiEvent.ShowError(UiError.NothingFound)
            }
        }
    }

    private fun renderState(state: VacancyScreenState) {
        _vacancyStatusLiveData.postValue(state)
    }
}
