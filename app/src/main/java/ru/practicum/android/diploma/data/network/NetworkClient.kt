package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.data.dto.NetworkResponse
import ru.practicum.android.diploma.data.dto.requests.VacanciesSearchRequest
import ru.practicum.android.diploma.data.dto.requests.VacancyByIdRequest
import ru.practicum.android.diploma.data.dto.responses.FilterAreaDTO
import ru.practicum.android.diploma.data.dto.responses.FilterIndustryDTO
import ru.practicum.android.diploma.data.dto.responses.VacanciesSearchResponse
import ru.practicum.android.diploma.data.dto.responses.VacancyDetailDTO

interface NetworkClient {
    suspend fun findVacancies(dto: VacanciesSearchRequest): NetworkResponse<VacanciesSearchResponse>
    suspend fun getVacancyById(dto: VacancyByIdRequest): NetworkResponse<VacancyDetailDTO>
    suspend fun getAreas(): NetworkResponse<List<FilterAreaDTO>>
    suspend fun getIndustries(): NetworkResponse<List<FilterIndustryDTO>>
}
