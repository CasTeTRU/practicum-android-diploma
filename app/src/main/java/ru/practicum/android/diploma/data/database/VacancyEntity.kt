package ru.practicum.android.diploma.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity класс для хранения избранных вакансий в Room Database
 * Хранит полные данные вакансии в формате JSON для офлайн-доступа
 */
@Entity(tableName = "favorite_vacancies")
data class VacancyEntity(
    @PrimaryKey
    val id: String,
    val vacancyJson: String // JSON строка с полными данными VacancyDetailDTO
)

