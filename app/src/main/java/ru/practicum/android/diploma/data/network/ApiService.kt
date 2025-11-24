package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Базовый интерфейс для API запросов
 * Здесь будут определены методы для работы с API вакансий
 */
interface ApiService {
    // Пример методов API (будут добавлены по мере необходимости):
    // @GET("vacancies")
    // suspend fun searchVacancies(
    //     @Query("text") query: String,
    //     @Query("page") page: Int = 0,
    //     @Query("per_page") perPage: Int = 20
    // ): Response<VacanciesResponse>
    
    // @GET("vacancies/{id}")
    // suspend fun getVacancyById(@Path("id") id: String): Response<VacancyDto>
}

