package ru.practicum.android.diploma.util

sealed class UiEvent {
    data class ShowMessage(val message: String) : UiEvent()
    data class ShowError(val error: UiError) : UiEvent()
}
