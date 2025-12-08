package ru.practicum.android.diploma.search.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.data.ApiError
import ru.practicum.android.diploma.data.ResponseCodes
import ru.practicum.android.diploma.filters.domain.api.FiltersInteractor
import ru.practicum.android.diploma.search.domain.api.SearchInteractor
import ru.practicum.android.diploma.search.domain.models.VacanciesPage
import ru.practicum.android.diploma.util.UiError
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(
    private val searchInteractor: SearchInteractor,
    private val filtersInteractor: FiltersInteractor
) : ViewModel() {
    private var _searchStatusLiveData = MutableLiveData(SearchScreenState())
    val searchStatusLiveData: LiveData<SearchScreenState> = _searchStatusLiveData

    private val searchDebounce = debounce<String>(
        SEARCH_DEBOUNCE_DELAY,
        viewModelScope,
        true
    ) { query ->
        startSearch(query)
    }

    init {
        loadSavedFilters()
    }

    fun loadSavedFilters() {
        viewModelScope.launch {
            try {
                val savedFilters = filtersInteractor.getFilterSettings()
                val hasFilters = savedFilters.industry != null || savedFilters.salary != null || savedFilters.onlyWithSalary
                updateState { it.copy(hasFilters = hasFilters) }
            } catch (e: RuntimeException) {
                Log.w(TAG, "loadSavedIndustry failed", e)
                updateState { it.copy(error = UiError.Unknown(0)) }
            }
        }
    }

    fun onQueryChanged(query: String) {
        updateState { cur ->
            if (cur.query == query) return@updateState cur

            cur.copy(query = query, page = 1, canLoadMore = true, error = null)
        }
        searchDebounce(query)
    }

    private fun startSearch(query: String) {
        if (query.isEmpty()) {
            updateState { cur ->
                cur.copy(
                    query = "",
                    vacancies = emptyList(),
                    found = 0,
                    isLoading = false,
                    isFetching = false,
                    error = null,
                    page = 1,
                    canLoadMore = true
                )
            }
            return
        }

        updateState {
            it.copy(
                isLoading = true,
                error = null,
                vacancies = emptyList(),
                page = 1,
                canLoadMore = true
            )
        }

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
        updateState { it.copy(isFetching = true) }

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
        updateState { cur ->
            if (reset && page.vacancies.isEmpty()) {
                return@updateState cur.copy(
                    vacancies = emptyList(),
                    found = page.found,
                    isLoading = false,
                    isFetching = false,
                    error = UiError.NothingFound,
                    page = 1,
                    canLoadMore = false
                )
            }

            val newList = if (reset) page.vacancies else cur.vacancies + page.vacancies
            cur.copy(
                vacancies = newList,
                found = page.found,
                isLoading = false,
                isFetching = false,
                error = null,
                page = if (reset) 1 else cur.page + 1,
                canLoadMore = page.vacancies.isNotEmpty()
            )
        }
    }

    private fun handleError(throwable: Throwable) {
        Log.d(TAG, "search error: $throwable")
        val uiError: UiError = when (throwable) {
            is java.net.UnknownHostException -> UiError.NoInternet
            is ApiError -> when (throwable.code) {
                ResponseCodes.ERROR_NO_INTERNET -> UiError.NoInternet
                ResponseCodes.NOTHING_FOUND -> UiError.NothingFound
                ResponseCodes.IO_EXCEPTION,
                ResponseCodes.MAPPER_EXCEPTION -> UiError.ServerError

                else -> UiError.Unknown(throwable.code)
            }

            else -> UiError.Unknown(ResponseCodes.IO_EXCEPTION)
        }

        updateState { it.copy(isLoading = false, isFetching = false, error = uiError) }
    }

    private fun updateState(update: (SearchScreenState) -> SearchScreenState) {
        val current = _searchStatusLiveData.value ?: SearchScreenState()
        _searchStatusLiveData.value = update(current)
    }

    companion object {
        private const val TAG = "SearchViewModel"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
//fixed
