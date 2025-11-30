package ru.practicum.android.diploma.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
/**
 * Entity класс для хранения избранных вакансий в Room Database
 * Содержит все данные, необходимые для отображения информации о вакансии
 */
@Entity(tableName = "favorite_vacancies")
@TypeConverters(VacancyTypeConverters::class)
data class VacancyEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val salaryFrom: Int?,
    val salaryTo: Int?,
    val salaryCurrency: String?,
    val address: String?,
    val city: String?,
    val experienceId: String?,
    val experienceName: String?,
    val scheduleId: String?,
    val scheduleName: String?,
    val employmentId: String?,
    val employmentName: String?,
    val employerId: String,
    val employerName: String,
    val employerLogo: String?,
    val areaId: Int,
    val areaName: String,
    val skills: String,
    val url: String,
    val industryId: Int?,
    val industryName: String?
)
