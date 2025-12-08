package ru.practicum.android.diploma.filters.data

import android.content.SharedPreferences
import com.google.gson.Gson
import ru.practicum.android.diploma.filters.domain.models.FiltersParameters

class FilterStorage(
    private val sharedPreferences: SharedPreferences,
    private val gson: Gson
) {
    fun saveFilters(filters: FiltersParameters) {
        val json = gson.toJson(filters)
        sharedPreferences.edit().putString(KEY_FILTERS, json).apply()
    }

    fun getFilters(): FiltersParameters? {
        val json = sharedPreferences.getString(KEY_FILTERS, null) ?: return null
        return try {
            gson.fromJson(json, FiltersParameters::class.java)
        } catch (e: com.google.gson.JsonSyntaxException) {
            // Если данные повреждены, очищаем их
            clearFilters()
            null
        }
    }

    fun clearFilters() {
        sharedPreferences.edit().remove(KEY_FILTERS).apply()
    }

    companion object {
        private const val KEY_FILTERS = "filters_parameters"
    }
}
