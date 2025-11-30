package ru.practicum.android.diploma.vacancy.domain.models

data class VacancyShow(
    val id: String,
    val name: String,
    val description: String,
    val salary: String,
    val address: String,
    val experience: String,
    val schedule: String,
    val employment: String,
    val contacts: Contacts?,
    val employerName: String,
    val skills: String?,
    val urlLogo: String?, // URL-adrress for logo company
    val isFavorite: Boolean,
    val urlLink: String // URL-adress to vacancy
)
