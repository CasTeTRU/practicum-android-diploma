package ru.practicum.android.diploma.search.domain.models

data class Address(
    val city: String,
    val street: String,
    val building: String,
    val fullAddress: String?
)
