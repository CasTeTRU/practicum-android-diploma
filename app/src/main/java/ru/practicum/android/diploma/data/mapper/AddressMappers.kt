package ru.practicum.android.diploma.data.mapper

import ru.practicum.android.diploma.data.dto.responses.AddressDTO
import ru.practicum.android.diploma.domain.models.Address

fun AddressDTO.toDomain(): Address? = Address(
    city = city,
    street = street,
    building = building,
    fullAddress = raw,
)
