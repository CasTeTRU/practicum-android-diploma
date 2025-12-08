package ru.practicum.android.diploma.filters.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.filters.domain.api.FiltersStorage
import ru.practicum.android.diploma.filters.domain.models.FiltersParameters

class FiltersStorageImpl(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) : FiltersStorage {

    private var currentState: FiltersParameters = getFilterSettings()
    private var prevState: FiltersParameters? = null

    private fun defaultFilters() = FiltersParameters(
        salary = null,
        onlyWithSalary = false,
        industry = null,
        area = null
    )

    override fun getFilterSettings(): FiltersParameters {
        val json = sharedPreferences.getString(FILTER_SETTINGS_KEY, null) ?: return defaultFilters()
        return try {
            gson.fromJson(json, FiltersParameters::class.java) ?: defaultFilters()
        } catch (e: Exception) {
            defaultFilters()
        }
    }

    override fun saveFilterSettings(settings: FiltersParameters) {
        saveState(settings)
    }

    private fun saveState(settings: FiltersParameters) {
        val json = gson.toJson(settings)
        sharedPreferences.edit { putString(FILTER_SETTINGS_KEY, json) }
        currentState = settings
    }

    // Сохраняем предыдущее состояние перед изменением
    private fun updateState(update: FiltersParameters.() -> FiltersParameters) {
        prevState = currentState.copy() // сохраняем предыдущие значения
        currentState = currentState.update()
        saveState(currentState)
    }

    override fun saveIndustry(industry: FilterIndustry) {
        updateState { copy(industry = industry) }
    }

    override fun clearIndustry() {
        updateState { copy(industry = null) }
    }

    override fun saveSalary(salary: Int?) {
        updateState { copy(salary = salary) }
    }

    override fun clearSalary() {
        updateState { copy(salary = null) }
    }

    override fun saveArea(area: String?) {
        updateState { copy(area = area) }
    }

    override fun clearArea() {
        updateState { copy(area = null) }
    }

    override fun saveOnlyWithSalary(onlyWithSalary: Boolean) {
        updateState { copy(onlyWithSalary = onlyWithSalary) }
    }

    // Метод отката к предыдущему состоянию
    override fun restorePreviousState() {
        prevState?.let {
            currentState = it
            saveState(it)
            prevState = null
        }
    }

    override fun clearFilterSettings() {
        prevState = currentState.copy()
        saveState(defaultFilters())
    }

    // Геттеры остаются такими же
    override fun getIndustry(): FilterIndustry? = currentState.industry
    override fun getSalary(): Int? = currentState.salary
    override fun getArea(): String? = currentState.area
    override fun getOnlyWithSalary(): Boolean = currentState.onlyWithSalary

    private companion object {
        const val FILTER_SETTINGS_KEY = "filters_parameters"
    }
}
