package ru.practicum.android.diploma.data.mapper

import ru.practicum.android.diploma.data.dto.responses.ContactsDTO
import ru.practicum.android.diploma.domain.models.Contacts

fun ContactsDTO.toDomain(): Contacts = Contacts(
    email = email,
    name = name,
    phones = phones.map { it.toDomain() }
)
