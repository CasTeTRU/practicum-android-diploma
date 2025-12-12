package ru.practicum.android.diploma.search.presentation

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
            val savedFilters = filtersInteractor.getFilterSettings()
            val hasFilters = savedFilters.industry != null ||
                savedFilters.salary != null ||
                savedFilters.onlyWithSalary
            updateState { it.copy(hasFilters = hasFilters) }
        }
    }

    fun refreshSearchIfActive() {
        val current = _searchStatusLiveData.value ?: return
        val query = current.query
        if (query.isNotBlank()) {
            startSearch(query)
        }
    }

    fun onQueryChanged(query: String) {
        val current = _searchStatusLiveData.value ?: SearchScreenState()
        if (current.query != query) {
            updateState { it.copy(query = query, page = 1, canLoadMore = true, error = null, isPaginationError = false) }
            searchDebounce(query)
        }
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
                    isPaginationError = false,
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
                isPaginationError = false,
                vacancies = emptyList(),
                page = 1,
                canLoadMore = true
            )
        }

        viewModelScope.launch {
            val savedFilters = filtersInteractor.getFilterSettings()
            searchInteractor.getVacancies(query = query, page = 1, filters = savedFilters)
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
        updateState { it.copy(isFetching = true, isPaginationError = false) }

        viewModelScope.launch {
            val savedFilters = filtersInteractor.getFilterSettings()
            searchInteractor.getVacancies(query = cur.query, page = nextPage, filters = savedFilters)
                .collect { result ->
                    result.fold(
                        onSuccess = { page -> handleSuccess(page = page, reset = false) },
                        onFailure = { throwable -> handleError(throwable) }
                    )
                }
        }
    }

    private fun handleSuccess(page: VacanciesPage, reset: Boolean) {
        val current = _searchStatusLiveData.value ?: SearchScreenState()
        val newState = if (reset && page.vacancies.isEmpty()) {
            current.copy(
                vacancies = emptyList(),
                found = page.found,
                isLoading = false,
                isFetching = false,
                error = UiError.NothingFound,
                isPaginationError = false,
                page = 1,
                canLoadMore = false
            )
        } else {
            val newList = if (reset) page.vacancies else current.vacancies + page.vacancies
            current.copy(
                vacancies = newList,
                found = page.found,
                isLoading = false,
                isFetching = false,
                error = null,
                isPaginationError = false,
                page = if (reset) 1 else current.page + 1,
                canLoadMore = page.vacancies.isNotEmpty()
            )
        }
        _searchStatusLiveData.value = newState
    }

    private fun handleError(throwable: Throwable) {
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
        val current = _searchStatusLiveData.value ?: SearchScreenState()
        val isPaginationNoInternetError = current.isFetching && uiError == UiError.NoInternet

        updateState { it.copy(
            isLoading = false,
            isFetching = false,
            error = if (isPaginationNoInternetError) null else uiError,
            isPaginationError = isPaginationNoInternetError
        ) }
    }

    private fun updateState(update: (SearchScreenState) -> SearchScreenState) {
        val current = _searchStatusLiveData.value ?: SearchScreenState()
        _searchStatusLiveData.value = update(current)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
