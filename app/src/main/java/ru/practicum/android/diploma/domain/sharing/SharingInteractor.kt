package ru.practicum.android.diploma.domain.sharing

import ru.practicum.android.diploma.domain.sharing.models.SharingIntent

interface SharingInteractor {
    fun shareVacancy(url: String, vacancyName: String): SharingIntent
    fun sendOnEmail(email: String): SharingIntent
    fun call(number: String): SharingIntent
}
