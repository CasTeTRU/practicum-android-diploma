package ru.practicum.android.diploma.data.mapper

import ru.practicum.android.diploma.data.dto.responses.FilterIndustryDTO
import ru.practicum.android.diploma.domain.models.FilterIndustry

fun FilterIndustryDTO.toDomain(): FilterIndustry = FilterIndustry(
    id = id,
    name = name
)
