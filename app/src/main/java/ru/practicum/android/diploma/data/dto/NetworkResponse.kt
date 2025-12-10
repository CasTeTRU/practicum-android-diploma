package ru.practicum.android.diploma.data.dto

data class NetworkResponse<T>(
    val data: T? = null,
    val resultCode: Int = 0
)
