package ru.practicum.android.diploma.filters.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.data.ApiError
import ru.practicum.android.diploma.data.ResponseCodes
import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.filters.domain.api.FiltersInteractor
import ru.practicum.android.diploma.util.UiError
import ru.practicum.android.diploma.util.debounce

class IndustryViewModel(
    private val filtersInteractor: FiltersInteractor,
) : ViewModel() {
    private val _allIndustries = mutableListOf<FilterIndustry>()
    private var _industryStatusLiveData = MutableLiveData<IndustryScreenState>()
    val industryStatusLiveData: LiveData<IndustryScreenState> = _industryStatusLiveData
    private val searchDebounce = debounce<String>(
        SEARCH_DEBOUNCE_DELAY,
        viewModelScope,
        true
    ) { query ->
        performSearch(query)
    }

    init {
        loadIndustry()
        loadSavedIndustry()
    }

    // --- Public API ---

    fun onSearchInput(query: String) {
        // update query immediately in state so UI can reflect the text
        updateState { it.copy(query = query) }
        searchDebounce(query)
    }

    fun selectIndustry(industry: FilterIndustry) {
        updateState {
            val newSelected = if (it.selected?.id == industry.id) null else industry
            it.copy(selected = newSelected)
        }
    }

    fun applySelection() {
        val selected = _industryStatusLiveData.value?.selected ?: return
        viewModelScope.launch {
            filtersInteractor.saveIndustry(selected)
        }
    }

    // --- Internal logic ---

    private fun loadIndustry() {
        updateState { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            filtersInteractor.loadIndustries().collect { result ->
                result.fold(
                    onSuccess = { industries -> handleSuccess(industries) },
                    onFailure = { throwable -> handleError(throwable) }
                )
            }
        }
    }

    private fun loadSavedIndustry() {
        viewModelScope.launch {
            val savedIndustry = filtersInteractor.getIndustry()
            if (savedIndustry != null) {
                updateState { it.copy(selected = savedIndustry) }
            }
        }
    }

    private fun handleSuccess(industries: List<FilterIndustry>) {
        _allIndustries.clear()
        _allIndustries.addAll(industries)

        if (industries.isEmpty()) {
            updateState {
                it.copy(
                    isLoading = false,
                    industryList = emptyList(),
                    error = UiError.NothingFound,
                    selected = null
                )
            }
            return
        }

        // preserve selected if still present in new list
        val prevSelected = _industryStatusLiveData.value?.selected
        val preservedSelected = prevSelected?.let { prev -> industries.find { it.id == prev.id } }

        updateState {
            it.copy(
                isLoading = false,
                industryList = industries,
                selected = preservedSelected,
                error = null
            )
        }
    }

    private fun performSearch(query: String) {
        val filtered = if (query.isBlank()) {
            _allIndustries.toList()
        } else {
            _allIndustries.filter { it.name.contains(query, ignoreCase = true) }
        }

        if (filtered.isEmpty()) {
            updateState {
                it.copy(
                    industryList = emptyList(),
                    selected = null,
                    error = UiError.NothingFound
                )
            }
        } else {
            // keep selected only if present in filtered
            val prevSelected = _industryStatusLiveData.value?.selected
            val selectedInFiltered =
                prevSelected?.let { prev -> filtered.find { it.id == prev.id } }

            updateState {
                it.copy(
                    industryList = filtered,
                    selected = selectedInFiltered,
                    error = null
                )
            }
        }
    }

    private fun handleError(throwable: Throwable) {
        Log.d(TAG, "load/search error: $throwable")
        val uiError = mapToUiError(throwable)
        updateState {
            it.copy(
                isLoading = false,
                industryList = emptyList(),
                selected = null,
                error = uiError
            )
        }
    }

    private fun mapToUiError(throwable: Throwable): UiError {
        return when (throwable) {
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
    }

    // --- Helpers ---

    private inline fun updateState(update: (IndustryScreenState) -> IndustryScreenState) {
        val current = _industryStatusLiveData.value ?: IndustryScreenState()
        _industryStatusLiveData.value = update(current)
    }

    companion object {
        private const val TAG = "IndustryViewModel"
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
