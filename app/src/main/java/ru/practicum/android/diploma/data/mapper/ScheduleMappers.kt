package ru.practicum.android.diploma.data.mapper

import ru.practicum.android.diploma.data.dto.responses.ScheduleDTO
import ru.practicum.android.diploma.domain.models.Schedule

fun ScheduleDTO.toDomain(): Schedule = Schedule(
    id = id,
    name = name
)
