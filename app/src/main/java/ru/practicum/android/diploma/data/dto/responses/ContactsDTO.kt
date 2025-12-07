package ru.practicum.android.diploma.data.dto.responses

data class ContactsDTO(
    val id: String,
    val name: String,
    val email: String,
    val phones: List<PhoneDTO>
)
