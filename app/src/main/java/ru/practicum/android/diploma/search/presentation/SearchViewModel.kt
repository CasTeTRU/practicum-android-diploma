package ru.practicum.android.diploma.search.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.data.ApiError
import ru.practicum.android.diploma.data.ResponceCodes
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.models.VacanciesPage
import ru.practicum.android.diploma.util.UiError
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
) : ViewModel() {
    private var _searchStatusLiveData = MutableLiveData<SearchScreenState>()
    val searchStatusLiveData: LiveData<SearchScreenState> = _searchStatusLiveData

    private val searchDebounce = debounce<String>(
        SEARCH_DEBOUNCE_DELAY,
        viewModelScope,
        true
    ) { query ->
        startSearch(query)
    }

    fun onQueryChanged(query: String) {
        val cur = _searchStatusLiveData.value ?: SearchScreenState()
        if (cur.query == query) return

        _searchStatusLiveData.value = cur.copy(query = query, page = 1, canLoadMore = true, error = null)
        searchDebounce(query)
    }

    private fun startSearch(query: String) {
        if (query.isEmpty()) {
            _searchStatusLiveData.value = SearchScreenState()
            return
        }

        _searchStatusLiveData.value = _searchStatusLiveData.value?.copy(
            isLoading = true,
            error = null,
            vacancies = emptyList(),
            page = 1,
            canLoadMore = true
        )

        viewModelScope.launch {
            searchInteractor.getVacancies(query = query, page = 1)
                .collect { result ->
                    result.fold(
                        onSuccess = { page -> handleSuccess(page = page, reset = true) },
                        onFailure = { throwable -> handleError(throwable) }
                    )
                }
        }
    }

    fun fetchNextPage() {
        val cur = _searchStatusLiveData.value ?: return
        val isCurrentlyLoading = cur.isLoading || cur.isFetching
        val cannotLoadMore = !cur.canLoadMore
        val isQueryEmpty = cur.query.isBlank()

        if (isCurrentlyLoading || cannotLoadMore || isQueryEmpty) return

        val nextPage = cur.page + 1
        _searchStatusLiveData.value = cur.copy(isFetching = true) // only fetching flag

        viewModelScope.launch {
            searchInteractor.getVacancies(query = cur.query, page = nextPage)
                .collect { result ->
                    result.fold(
                        onSuccess = { page -> handleSuccess(page = page, reset = false) },
                        onFailure = { throwable -> handleError(throwable) }
                    )
                }
        }
    }

    private fun handleSuccess(page: VacanciesPage, reset: Boolean) {
        val cur = _searchStatusLiveData.value ?: SearchScreenState()

        // Если это первая загрузка и список пустой, показываем плейсхолдер
        if (reset && page.vacancies.isEmpty()) {
            _searchStatusLiveData.value = cur.copy(
                vacancies = emptyList(),
                found = page.found,
                isLoading = false,
                isFetching = false,
                error = UiError.NothingFound,
                page = 1,
                canLoadMore = false
            )
            return
        }
        val newList = if (reset) {
            page.vacancies
        } else {
            cur.vacancies + page.vacancies
        }

        _searchStatusLiveData.value = cur.copy(
            vacancies = newList,
            found = page.found,
            isLoading = false,
            isFetching = false,
            error = null,
            page = if (reset) 1 else cur.page + 1,
            canLoadMore = page.vacancies.isNotEmpty()
        )
    }

    private fun handleError(throwable: Throwable) {
        Log.d(TAG, "search error: $throwable")
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

        val cur = _searchStatusLiveData.value ?: SearchScreenState()
        _searchStatusLiveData.value = cur.copy(
            isLoading = false,
            isFetching = false,
            error = uiError
        )
    }

    companion object {
        private const val TAG = "SearchViewModel"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
