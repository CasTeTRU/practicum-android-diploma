package ru.practicum.android.diploma.domain.sharing.models

import android.content.Intent

data class SharingIntent(
    val intent: Intent,
    val error: String
)
