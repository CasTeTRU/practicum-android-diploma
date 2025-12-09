package ru.practicum.android.diploma.data.mapper

import ru.practicum.android.diploma.data.dto.responses.ExperienceDTO
import ru.practicum.android.diploma.domain.models.Experience

fun ExperienceDTO.toDomain(): Experience = Experience(
    id = id,
    name = name,
)
