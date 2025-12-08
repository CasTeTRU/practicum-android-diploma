package ru.practicum.android.diploma.filters.data

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
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
        } catch (e: JsonSyntaxException) {
            Log.e("FiltersStorage", "Failed to parse filters JSON", e)
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

    // Метод отката к предыдущему состоянию
    override fun restorePreviousState() {
        prevState?.let {
            currentState = it
            saveState(it)
            prevState = null
        }
    }

    override fun clearFilterSettings() {
        prevState = null
        saveState(defaultFilters())
    }

    override fun getIndustry(): FilterIndustry? = currentState.industry

    private companion object {
        const val FILTER_SETTINGS_KEY = "filters_parameters"
    }
}
