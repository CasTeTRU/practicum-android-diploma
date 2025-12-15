package ru.practicum.android.diploma.domain.sharing.api

interface SharingRepository {
    fun shareVacancy(url: String)
    fun sendOnEmail(email: String)
    fun call(number: String)
}
