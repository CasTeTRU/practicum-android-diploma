package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.data.dto.NetworkResponse
import ru.practicum.android.diploma.data.dto.requests.VacanciesSearchRequest
import ru.practicum.android.diploma.data.dto.requests.VacancyByIdRequest

interface NetworkClient {
    suspend fun findVacancies(dto: VacanciesSearchRequest): NetworkResponse
    suspend fun getVacancyById(dto: VacancyByIdRequest): NetworkResponse
    suspend fun getAreas(): NetworkResponse
    suspend fun getIndustries(): NetworkResponse
}
