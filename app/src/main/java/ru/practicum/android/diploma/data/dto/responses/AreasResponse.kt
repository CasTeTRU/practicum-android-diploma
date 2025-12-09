package ru.practicum.android.diploma.data.dto.responses

import ru.practicum.android.diploma.data.dto.NetworkResponse

data class AreasResponse(
    val areas: List<FilterAreaDTO>? = null
) : NetworkResponse()
