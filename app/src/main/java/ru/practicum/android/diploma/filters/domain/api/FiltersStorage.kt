package ru.practicum.android.diploma.filters.domain.api

import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.filters.domain.models.FiltersParameters

interface FiltersStorage {
    /** Получить текущие настройки фильтров, возвращает дефолтные значения если нет настроек */
    fun getFilterSettings(): FiltersParameters

    /** Сохранить настройки фильтров */
    fun saveFilterSettings(settings: FiltersParameters)

    /** Очистить все настройки фильтров */
    fun clearFilterSettings()

    fun restorePreviousState()

    // --- Вспомогательные методы для отдельных полей ---

    fun getIndustry(): FilterIndustry?
    fun saveIndustry(industry: FilterIndustry)
    fun clearIndustry()

    fun getSalary(): Int?
    fun saveSalary(salary: Int?)
    fun clearSalary()

    fun getArea(): String?
    fun saveArea(area: String?)
    fun clearArea()

    fun getOnlyWithSalary(): Boolean
    fun saveOnlyWithSalary(onlyWithSalary: Boolean)
}
