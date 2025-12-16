package ru.practicum.android.diploma.data.dto.responses

class FilterAreaDTO(
    val id: Int,
    val name: String,
    val parentId: Int,
    val areas: List<FilterAreaDTO>
)
