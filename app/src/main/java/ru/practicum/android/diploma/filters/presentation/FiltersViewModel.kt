package ru.practicum.android.diploma.filters.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filters.domain.api.FiltersInteractor
import ru.practicum.android.diploma.filters.domain.models.FiltersParameters

class FiltersViewModel(
    private val filtersInteractor: FiltersInteractor
) : ViewModel() {
    private val _filtersState = MutableLiveData<FilterScreenState>(FilterScreenState())
    val filtersState: LiveData<FilterScreenState> = _filtersState

    init {
        loadSavedFilters()
    }

    fun restorePreviousState() {
        viewModelScope.launch {
            filtersInteractor.restorePreviousState()
        }
    }

    fun clearIndustry() {
        updateState { it.copy(industry = null) }
        viewModelScope.launch {
            filtersInteractor.clearIndustry()
        }
    }

    fun updateIndustry(industry: ru.practicum.android.diploma.domain.models.FilterIndustry?) {
        updateState { it.copy(industry = industry) }
    }

    fun updateSalary(salary: Int?) {
        updateState { it.copy(salary = salary) }
    }

    fun updateOnlyWithSalary(onlyWithSalary: Boolean) {
        updateState { it.copy(onlyWithSalary = onlyWithSalary) }
    }

    fun applyFilters() {
        val state = _filtersState.value ?: return
        viewModelScope.launch {
            filtersInteractor.saveFilterSettings(
                FiltersParameters(
                    industry = state.industry,
                    salary = state.salary,
                    onlyWithSalary = state.onlyWithSalary
                )
            )
        }
    }

    fun clearSelection() {
        _filtersState.value = FilterScreenState()
        viewModelScope.launch {
            filtersInteractor.resetFilterSettings()
        }
    }

    fun loadSavedFilters() {
        viewModelScope.launch {
            val savedFilters = filtersInteractor.getFilterSettings()
            updateState {
                it.copy(
                    industry = savedFilters.industry,
                    salary = savedFilters.salary,
                    onlyWithSalary = savedFilters.onlyWithSalary
                )
            }
        }
    }

    // --- Internal logic ---

    private inline fun updateState(update: (FilterScreenState) -> FilterScreenState) {
        val current = _filtersState.value ?: FilterScreenState()
        _filtersState.value = update(current)
    }

    companion object {
        private const val TAG = "FiltersViewModel"
    }
}
