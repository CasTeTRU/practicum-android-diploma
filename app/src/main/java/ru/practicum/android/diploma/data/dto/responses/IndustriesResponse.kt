package ru.practicum.android.diploma.data.dto.responses

import ru.practicum.android.diploma.data.dto.NetworkResponse

data class IndustriesResponse(
    val industries: List<FilterIndustryDTO>? = null
) : NetworkResponse()
