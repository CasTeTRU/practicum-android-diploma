package ru.practicum.android.diploma.filters.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.filters.domain.api.FilterInteractor
import ru.practicum.android.diploma.filters.domain.api.IndustryInteractor
import ru.practicum.android.diploma.filters.domain.models.FiltersParameters

class FiltersViewModel(
    private val filterInteractor: FilterInteractor,
    private val industryInteractor: IndustryInteractor
) : ViewModel() {

    private val _filtersState = MutableLiveData<FiltersScreenState>()
    val filtersState: LiveData<FiltersScreenState> = _filtersState

    private var industriesCache: List<FilterIndustry>? = null

    init {
        loadFilters()
    }

    private fun loadFilters() {
        val filters = filterInteractor.getFilters() ?: DEFAULT_FILTERS
        _filtersState.value = FiltersScreenState(filters)
        if (filters.industry != null) {
            loadIndustryName(filters.industry)
        }
    }

    private fun loadIndustryName(industryId: Int) {
        // Используем кеш, если он есть
        industriesCache?.let { industries ->
            val industry = industries.find { it.id == industryId }
            updateIndustryName(industry?.name)
            return
        }

        // Загружаем отрасли, если кеша нет
        viewModelScope.launch {
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

    private fun updateIndustryName(industryName: String?) {
        val currentState = _filtersState.value ?: return
        _filtersState.value = currentState.copy(industryName = industryName)
    }

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
