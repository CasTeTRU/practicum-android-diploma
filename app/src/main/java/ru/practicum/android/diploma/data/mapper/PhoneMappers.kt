package ru.practicum.android.diploma.data.mapper

import ru.practicum.android.diploma.data.dto.responses.PhoneDTO
import ru.practicum.android.diploma.domain.models.Phone

fun PhoneDTO.toDomain(): Phone = Phone(
    comment = comment,
    formatted = formatted
)
