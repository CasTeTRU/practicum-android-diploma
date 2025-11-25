package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.data.dto.NetworkResponse
import ru.practicum.android.diploma.data.dto.requests.VacanciesSearchRequest

interface NetworkClient {
    suspend fun findVacancies(dto: VacanciesSearchRequest): NetworkResponse
    suspend fun getVacancyById(id: String): NetworkResponse
    suspend fun getAreas(): NetworkResponse
    suspend fun getIndustries(): NetworkResponse
}
