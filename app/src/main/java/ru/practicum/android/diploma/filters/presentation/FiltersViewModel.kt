package ru.practicum.android.diploma.filters.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filters.domain.api.FiltersInteractor
import ru.practicum.android.diploma.filters.domain.models.FiltersParameters
import ru.practicum.android.diploma.util.UiError

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
    fun updateSalary(salary: Int?) {
        updateState { it.copy(salary = salary) }
    }

    fun updateOnlyWithSalary(onlyWithSalary: Boolean) {
        updateState { it.copy(onlyWithSalary = onlyWithSalary) }
    }

    fun applyFilters() {
        val state = _filtersState.value ?: return
        viewModelScope.launch {
            try {
                filtersInteractor.saveFilterSettings(
                    FiltersParameters(
                        industry = state.industry,
                        salary = state.salary,
                        onlyWithSalary = state.onlyWithSalary
                    )
                )
            } catch (t: Throwable) {
                Log.w(TAG, "applyFilters failed", t)
                updateState { it.copy(error = UiError.Unknown(0)) }
            }
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
            try {
                val savedFilters = filtersInteractor.getFilterSettings()
                updateState {
                    it.copy(
                        industry = savedFilters.industry,
                        salary = savedFilters.salary,
                        onlyWithSalary = savedFilters.onlyWithSalary
                    )
                }
            } catch (t: Throwable) {
                Log.w(TAG, "loadSavedIndustry failed", t)
                _filtersState.value = FilterScreenState(error = UiError.Unknown(0))
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
