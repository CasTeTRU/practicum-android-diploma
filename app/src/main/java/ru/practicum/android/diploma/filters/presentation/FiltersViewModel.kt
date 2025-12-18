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
    private val _hasChanges = MutableLiveData(false)
    val hasChanges: LiveData<Boolean> = _hasChanges
    private var appliedFilters: FiltersParameters = DEFAULT_FILTERS

    init {
        loadSavedFilters()
    }

    fun restorePreviousState() {
        viewModelScope.launch {
            filtersInteractor.restorePreviousState()
            loadFiltersFromStorage()
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
            val newFilters = state.toFiltersParameters()
            filtersInteractor.saveFilterSettings(newFilters)
            appliedFilters = newFilters
            checkForChanges()
        }
    }

    fun clearSelection() {
        viewModelScope.launch {
            filtersInteractor.resetFilterSettings()
            appliedFilters = DEFAULT_FILTERS
            updateState { FilterScreenState() }
        }
    }

    fun loadSavedFilters() {
        viewModelScope.launch {
            loadFiltersFromStorage()
        }
    }

    // --- Internal logic ---

    private suspend fun loadFiltersFromStorage() {
        val savedFilters = filtersInteractor.getFilterSettings()
        appliedFilters = savedFilters
        updateState {
            it.copy(
                industry = savedFilters.industry,
                salary = savedFilters.salary,
                onlyWithSalary = savedFilters.onlyWithSalary
            )
        }
    }

    private inline fun updateState(update: (FilterScreenState) -> FilterScreenState) {
        val current = _filtersState.value ?: FilterScreenState()
        _filtersState.value = update(current)
        checkForChanges()
    }
    private fun checkForChanges() {
        val current = _filtersState.value ?: FilterScreenState()
        val currentIndustryId = current.industry?.id
        val appliedIndustryId = appliedFilters.industry?.id
        val industryChanged = currentIndustryId != appliedIndustryId
        val salaryChanged = current.salary != appliedFilters.salary
        val onlyWithSalaryChanged = current.onlyWithSalary != appliedFilters.onlyWithSalary
        val hasChanges = industryChanged || salaryChanged || onlyWithSalaryChanged
        _hasChanges.value = hasChanges
    }
    private fun FilterScreenState.toFiltersParameters(): FiltersParameters {
        return FiltersParameters(
            industry = this.industry,
            salary = this.salary,
            onlyWithSalary = this.onlyWithSalary
        )
    }

    companion object {
        private val DEFAULT_FILTERS = FiltersParameters(
            industry = null,
            salary = null,
            onlyWithSalary = false
        )
    }
}
