package ru.practicum.android.diploma.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entity класс для хранения избранных вакансий в Room Database
 * Будет расширен после определения структуры данных вакансии
 */
@Entity(tableName = "favorite_vacancies")
data class VacancyEntity(
    @PrimaryKey
    val id: String,
    // Здесь будут добавлены остальные поля после определения структуры
    // Например: name, salary, employer и т.д.
)

