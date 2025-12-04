package ru.practicum.android.diploma.data.mapper

import ru.practicum.android.diploma.data.dto.responses.FilterAreaDTO
import ru.practicum.android.diploma.domain.models.FilterArea


fun FilterAreaDTO.toDomain(): FilterArea = FilterArea(
    id = id,
    name = name,
    parentId = parentId,
    areas = areas,
)
