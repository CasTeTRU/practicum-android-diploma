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

    private val _shouldShowApplyButton = MutableLiveData<Boolean>(false)
    val shouldShowApplyButton: LiveData<Boolean> = _shouldShowApplyButton

    private var allIndustries: List<FilterIndustry> = emptyList()
    private var filteredIndustries: List<FilterIndustry> = emptyList()
    private var isInitialLoad = true
    private var appliedIndustry: FilterIndustry? = null

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
                        // Загружаем сохраненную отрасль перед установкой состояния только при первой загрузке
                        if (isInitialLoad) {
                            loadSavedIndustry(industries)
                            isInitialLoad = false
                        }
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
        // Сбрасываем выбранную отрасль перед загрузкой примененной
        _selectedIndustry.value = null
        // Если appliedIndustry уже установлен (передан через аргументы), используем его
        if (appliedIndustry != null) {
            val foundIndustry = industries.find { it.id == appliedIndustry?.id }
            foundIndustry?.let {
                _selectedIndustry.value = it
                updateApplyButtonVisibility()
            } ?: run {
                appliedIndustry = null
                updateApplyButtonVisibility()
            }
        } else {
            // Иначе загружаем из хранилища
            val savedFilters = filtersInteractor.getFilterSettings()
            savedFilters.industry?.let { savedIndustry ->
                val foundIndustry = industries.find { it.id == savedIndustry.id }
                foundIndustry?.let {
                    appliedIndustry = it
                    _selectedIndustry.value = it
                    updateApplyButtonVisibility()
                }
            } ?: run {
                appliedIndustry = null
                updateApplyButtonVisibility()
            }
        }
    }

    fun onIndustrySelected(industry: FilterIndustry) {
        _selectedIndustry.value = industry
        updateApplyButtonVisibility()
    }

    private fun updateApplyButtonVisibility() {
        val selected = _selectedIndustry.value
        // Показываем кнопку только если выбранная отрасль отличается от примененной
        _shouldShowApplyButton.value = selected != null && selected.id != appliedIndustry?.id
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

    fun setCurrentIndustry(industry: FilterIndustry) {
        appliedIndustry = industry
        if (allIndustries.isNotEmpty()) {
            val foundIndustry = allIndustries.find { it.id == industry.id }
            foundIndustry?.let {
                _selectedIndustry.value = it
                updateApplyButtonVisibility()
            }
        }
    }

    fun resetToSavedIndustry() {
        if (allIndustries.isEmpty()) return
        viewModelScope.launch {
            // Сбрасываем выбранную отрасль и загружаем только примененную
            _selectedIndustry.value = null
            // Используем appliedIndustry, если он уже установлен, иначе загружаем из хранилища
            if (appliedIndustry != null) {
                val foundIndustry = allIndustries.find { it.id == appliedIndustry?.id }
                foundIndustry?.let {
                    _selectedIndustry.value = it
                }
            } else {
                val savedFilters = filtersInteractor.getFilterSettings()
                savedFilters.industry?.let { savedIndustry ->
                    val foundIndustry = allIndustries.find { it.id == savedIndustry.id }
                    foundIndustry?.let {
                        appliedIndustry = it
                        _selectedIndustry.value = it
                    }
                }
            }
            updateApplyButtonVisibility()
        }
    }
}

sealed class FilterIndustryScreenState {
    object Loading : FilterIndustryScreenState()
    data class Content(val industries: List<FilterIndustry>) : FilterIndustryScreenState()
    data class Error(val error: UiError) : FilterIndustryScreenState()
}
