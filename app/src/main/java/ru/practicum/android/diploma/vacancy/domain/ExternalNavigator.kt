package ru.practicum.android.diploma.vacancy.domain

interface ExternalNavigator {
    fun emailTo(email: String)
    fun callTo(phoneNumber: String)
    fun shareLink(alternateUrl: String)
}
