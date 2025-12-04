package ru.practicum.android.diploma.domain.models

import ru.practicum.android.diploma.data.dto.responses.FilterAreaDTO

data class FilterArea(
    val id: Int,
    val name: String,
    val parentId: Int,
    val areas: List<FilterAreaDTO>
)
