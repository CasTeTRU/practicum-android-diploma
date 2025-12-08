package ru.practicum.android.diploma.filters.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.filters.domain.api.FiltersInteractor
import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.filters.domain.api.FilterInteractor
import ru.practicum.android.diploma.filters.domain.api.IndustryInteractor
import ru.practicum.android.diploma.filters.domain.models.FiltersParameters

class FiltersViewModel(
    private val filtersInteractor: FiltersInteractor
    private val filterInteractor: FilterInteractor,
    private val industryInteractor: IndustryInteractor
) : ViewModel() {
    private val _filtersState = MutableLiveData<FilterScreenState>(FilterScreenState())
    val filtersState: LiveData<FilterScreenState> = _filtersState

    private val _filtersState = MutableLiveData<FiltersScreenState>()
    val filtersState: LiveData<FiltersScreenState> = _filtersState

    private var industriesCache: List<FilterIndustry>? = null

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
        loadFilters()
    }

    fun updateSalary(salary: Int?) {
        updateState { it.copy(salary = salary) }
    }

    fun updateOnlyWithSalary(onlyWithSalary: Boolean) {
        updateState { it.copy(onlyWithSalary = onlyWithSalary) }
    private fun loadFilters() {
        val filters = filterInteractor.getFilters() ?: DEFAULT_FILTERS
        _filtersState.value = FiltersScreenState(filters)
        if (filters.industry != null) {
            loadIndustryName(filters.industry)
        }
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
    private fun loadIndustryName(industryId: Int) {
        // Используем кеш, если он есть
        industriesCache?.let { industries ->
            val industry = industries.find { it.id == industryId }
            updateIndustryName(industry?.name)
            return
        }

    fun clearSelection() {
        _filtersState.value = FilterScreenState()
        // Загружаем отрасли, если кеша нет
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
            industryInteractor.getIndustries().collect { result ->
                result.fold(
                    onSuccess = { industries ->
                        industriesCache = industries
                        val industry = industries.find { it.id == industryId }
                        updateIndustryName(industry?.name)
                    },
                    onFailure = { }
                )
            }
        }
    }

    // --- Internal logic ---
    private fun updateIndustryName(industryName: String?) {
        val currentState = _filtersState.value ?: return
        _filtersState.value = currentState.copy(industryName = industryName)
    }

    private inline fun updateState(update: (FilterScreenState) -> FilterScreenState) {
        val current = _filtersState.value ?: FilterScreenState()
        _filtersState.value = update(current)
    fun saveIndustry(industry: FilterIndustry) {
        val currentFilters = filterInteractor.getFilters() ?: DEFAULT_FILTERS
        val updatedFilters = currentFilters.copy(industry = industry.id)
        filterInteractor.saveFilters(updatedFilters)
        _filtersState.value = FiltersScreenState(
            filters = updatedFilters,
            industryName = industry.name
        )
    }

    companion object {
        private const val TAG = "FiltersViewModel"
        private val DEFAULT_FILTERS = FiltersParameters(
            salary = null,
            onlyWithSalary = false,
            industry = null,
            area = null
        )
    }
}

data class FiltersScreenState(
    val filters: FiltersParameters,
    val industryName: String? = null
)
