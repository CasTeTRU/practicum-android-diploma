package ru.practicum.android.diploma.domain.vacancy.models

data class VacancyPresent(
    val id: String,
    val name: String,
    val salary: String,
    val url: String?,
    val employerName: String,
    val address: String,
    val experience: String,
    val skills: List<String>?,
    val description: String,
    val contacts: Contacts?,
    val urlLink: String,
    var isFavorite: Boolean = false
)
