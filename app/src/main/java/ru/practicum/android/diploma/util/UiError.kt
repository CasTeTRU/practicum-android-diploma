package ru.practicum.android.diploma.util

sealed class UiError {
    object NoInternet : UiError()
    object ServerError : UiError()
    object NothingFound : UiError()
    data class Unknown(val code: Int) : UiError()
}
