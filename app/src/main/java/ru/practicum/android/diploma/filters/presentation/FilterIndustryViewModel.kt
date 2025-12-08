package ru.practicum.android.diploma.filters.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.filters.domain.api.FiltersInteractor
import ru.practicum.android.diploma.filters.domain.api.IndustryInteractor
import ru.practicum.android.diploma.util.UiError

class FilterIndustryViewModel(
    private val industryInteractor: IndustryInteractor,
    private val filtersInteractor: FiltersInteractor
) : ViewModel() {

    private val _screenState = MutableLiveData<FilterIndustryScreenState>()
    val screenState: LiveData<FilterIndustryScreenState> = _screenState

    private val _selectedIndustry = MutableLiveData<FilterIndustry?>()
    val selectedIndustry: LiveData<FilterIndustry?> = _selectedIndustry

    private var allIndustries: List<FilterIndustry> = emptyList()
    private var filteredIndustries: List<FilterIndustry> = emptyList()

    init {
        loadIndustries()
    }

    private fun loadIndustries() {
        _screenState.value = FilterIndustryScreenState.Loading
        viewModelScope.launch {
            industryInteractor.getIndustries().collect { result ->
                result.fold(
                    onSuccess = { industries ->
                        allIndustries = industries
                        filteredIndustries = industries
                        // Загружаем сохраненную отрасль перед установкой состояния
                        loadSavedIndustry(industries)
                        _screenState.value = FilterIndustryScreenState.Content(industries)
                    },
                    onFailure = { throwable ->
                        val uiError = when (throwable) {
                            is java.net.UnknownHostException -> UiError.NoInternet
                            else -> UiError.ServerError
                        }
                        _screenState.value = FilterIndustryScreenState.Error(uiError)
                    }
                )
            }
        }
    }

    private suspend fun loadSavedIndustry(industries: List<FilterIndustry>) {
        val savedFilters = filtersInteractor.getFilterSettings()
        savedFilters.industry?.let { savedIndustry ->
            val foundIndustry = industries.find { it.id == savedIndustry.id }
            foundIndustry?.let {
                _selectedIndustry.value = it
            }
        }
    }

    fun onIndustrySelected(industry: FilterIndustry) {
        _selectedIndustry.value = industry
    }

    fun filterIndustries(query: String) {
        filteredIndustries = if (query.isBlank()) {
            allIndustries
        } else {
            allIndustries.filter { it.name.contains(query, ignoreCase = true) }
        }
        _screenState.value = FilterIndustryScreenState.Content(filteredIndustries)
    }

    fun getSelectedIndustry(): FilterIndustry? = _selectedIndustry.value
}

sealed class FilterIndustryScreenState {
    object Loading : FilterIndustryScreenState()
    data class Content(val industries: List<FilterIndustry>) : FilterIndustryScreenState()
    data class Error(val error: UiError) : FilterIndustryScreenState()
}
