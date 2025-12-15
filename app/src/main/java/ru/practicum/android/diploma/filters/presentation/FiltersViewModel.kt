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
    
    private var appliedFilters: FiltersParameters = FiltersParameters(
        industry = null,
        salary = null,
        onlyWithSalary = false
    )

    init {
        loadSavedFilters()
    }

    fun restorePreviousState() {
        viewModelScope.launch {
            filtersInteractor.restorePreviousState()
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
            val newFilters = FiltersParameters(
                industry = state.industry,
                salary = state.salary,
                onlyWithSalary = state.onlyWithSalary
            )
            filtersInteractor.saveFilterSettings(newFilters)
            appliedFilters = newFilters
            checkForChanges()
        }
    }

    fun clearSelection() {
        viewModelScope.launch {
            filtersInteractor.resetFilterSettings()
            appliedFilters = FiltersParameters(
                industry = null,
                salary = null,
                onlyWithSalary = false
            )
            updateState { FilterScreenState() }
        }
    }

    fun loadSavedFilters() {
        viewModelScope.launch {
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
    }

    // --- Internal logic ---

    private inline fun updateState(update: (FilterScreenState) -> FilterScreenState) {
        val current = _filtersState.value ?: FilterScreenState()
        _filtersState.value = update(current)
        checkForChanges()
    }
    
    private fun checkForChanges() {
        val current = _filtersState.value ?: FilterScreenState()
        
        val hasChanges = current.industry?.id != appliedFilters.industry?.id ||
                current.salary != appliedFilters.salary ||
                current.onlyWithSalary != appliedFilters.onlyWithSalary
        
        _hasChanges.value = hasChanges
    }

    companion object {
        private const val TAG = "FiltersViewModel"
    }
}
