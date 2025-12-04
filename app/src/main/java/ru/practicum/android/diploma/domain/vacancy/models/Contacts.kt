package ru.practicum.android.diploma.domain.vacancy.models

import ru.practicum.android.diploma.domain.models.Phone

data class Contacts(
    val name: String,
    val email: String,
    val phones: List<Phone>?
)
