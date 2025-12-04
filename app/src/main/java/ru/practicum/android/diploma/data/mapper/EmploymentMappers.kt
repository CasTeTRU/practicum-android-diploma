package ru.practicum.android.diploma.data.mapper

import ru.practicum.android.diploma.data.dto.responses.EmploymentDTO
import ru.practicum.android.diploma.domain.models.Employment

fun EmploymentDTO.toDomain(): Employment = Employment(
    id = id,
    name = name
)
