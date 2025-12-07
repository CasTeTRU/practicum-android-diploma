package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practicum.android.diploma.data.dto.responses.AreasResponse
import ru.practicum.android.diploma.data.dto.responses.IndustriesResponse
import ru.practicum.android.diploma.data.dto.responses.VacanciesSearchResponse
import ru.practicum.android.diploma.data.dto.responses.VacancyByIdResponse

interface ApiService {
    @GET("/vacancies/{vacancy_id}")
    suspend fun getVacancyById(@Path("vacancy_id") id: String): VacancyByIdResponse

    @GET("/vacancies")
    suspend fun findVacancies(
        @Query("text") text: String,
        @Query("area") area: String? = null,
        @Query("salary") salary: Int? = null,
        @Query("industry") industry: Int? = null,
        @Query("page") page: Int = 0,
        @Query("only_with_salary") onlyWithSalary: Boolean = false,
    ): VacanciesSearchResponse

    @GET("/areas")
    suspend fun getAreas(): AreasResponse

    @GET("/industries")
    suspend fun getIndustries(): IndustriesResponse
}
