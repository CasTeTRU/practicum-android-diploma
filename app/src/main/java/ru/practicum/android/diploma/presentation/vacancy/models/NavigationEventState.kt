package ru.practicum.android.diploma.presentation.vacancy.models

import android.content.Intent

data class NavigationEventState(
    val intent: Intent,
    val errorMessage: String
)
