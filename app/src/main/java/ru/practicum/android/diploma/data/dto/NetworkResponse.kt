package ru.practicum.android.diploma.data.dto

data class NetworkResponse<T>(
    var data: T? = null,
    var resultCode: Int = 0
)
