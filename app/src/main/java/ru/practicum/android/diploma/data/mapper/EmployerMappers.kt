package ru.practicum.android.diploma.data.mapper

import ru.practicum.android.diploma.data.dto.responses.EmployerDTO
import ru.practicum.android.diploma.domain.models.Employer

fun EmployerDTO.toDomain(): Employer = Employer(
    id = id,
    name = name,
    logo = logo
)
