package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.dto.responses.AreasResponse
import ru.practicum.android.diploma.data.dto.responses.IndustriesResponse
import ru.practicum.android.diploma.data.dto.responses.VacanciesSearchResponse
import ru.practicum.android.diploma.data.dto.responses.VacancyDetailResponse

interface ApiService {
    @Headers(
        "Authorization: Bearer ${BuildConfig.API_ACCESS_TOKEN}"
    )
    @GET("/vacancies/{vacancy_id}")
    suspend fun getVacancyById(@Path("vacancy_id") id: String): VacancyDetailResponse

    @Headers(
        "Authorization: Bearer ${BuildConfig.API_ACCESS_TOKEN}"
    )
    @GET("/vacancies")
    suspend fun findVacancies(
        @Query("text") text: String,
        @Query("area") area: String? = null,
        @Query("salary") salary: Int? = null,
        @Query("industry") industry: Int? = null,
        @Query("page") page: Int = 0,
        @Query("only_with_salary") onlyWithSalary: Boolean = false,
    ): VacanciesSearchResponse

    @Headers(
        "Authorization: Bearer ${BuildConfig.API_ACCESS_TOKEN}"
    )
    @GET("/areas")
    suspend fun getAreas(): AreasResponse

    @Headers(
        "Authorization: Bearer ${BuildConfig.API_ACCESS_TOKEN}"
    )
    @GET("/industries")
    suspend fun getIndustries(): IndustriesResponse
}
