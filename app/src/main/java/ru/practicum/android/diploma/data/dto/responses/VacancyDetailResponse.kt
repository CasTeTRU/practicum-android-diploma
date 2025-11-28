package ru.practicum.android.diploma.data.dto.responses

import ru.practicum.android.diploma.data.dto.NetworkResponse

data class VacancyDetailResponse(
    val vacancy: VacancyDetailDTO? = null
) : NetworkResponse()
