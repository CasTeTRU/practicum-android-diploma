package ru.practicum.android.diploma.search.data

import android.util.Log
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.ApiError
import ru.practicum.android.diploma.data.ResponceCodes
import ru.practicum.android.diploma.data.dto.requests.VacanciesSearchRequest
import ru.practicum.android.diploma.data.dto.responses.VacanciesSearchResponse
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.filters.domain.models.FiltersParameters
import ru.practicum.android.diploma.search.data.mapper.toDomain
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.search.domain.models.VacanciesPage

class SearchRepositoryImpl(
    private val networkClient: NetworkClient
) : SearchRepository {
    override suspend fun getVacancies(
        query: String,
        page: Int,
        filters: FiltersParameters?
    ): Flow<Result<VacanciesPage>> = flow {
        val vacancySearchRequest = VacanciesSearchRequest(
            query = query,
            area = filters?.area,
            salary = filters?.salary,
            industry = filters?.industry,
            page = page,
            onlyWithSalary = filters?.onlyWithSalary ?: false
        )
        val response = networkClient.findVacancies(vacancySearchRequest)

        when (response.resultCode) {
            ResponceCodes.ERROR_NO_INTERNET -> emit(Result.failure(ApiError(ResponceCodes.ERROR_NO_INTERNET)))
            ResponceCodes.IO_EXCEPTION -> emit(Result.failure(ApiError(ResponceCodes.IO_EXCEPTION)))
            ResponceCodes.SUCCESS_STATUS -> {
                val vacancyResponse = response as? VacanciesSearchResponse

                if (vacancyResponse?.items !== null) {
                    try {
                        val domain = vacancyResponse.toDomain()
                        emit(Result.success(domain))
                    } catch (t: IllegalArgumentException) {
                        Log.d(TAG_SEARCH_RESPONSE, "$vacancyResponse", t)
                        // Если маппер упал по какой-то причине
                        emit(Result.failure(ApiError(ResponceCodes.MAPPER_EXCEPTION)))
                    } catch (e: JsonSyntaxException) {
                        Log.d(TAG_SEARCH_RESPONSE, "$vacancyResponse", e)
                        emit(Result.failure(ApiError(ResponceCodes.MAPPER_EXCEPTION)))
                    }
                } else {
                    emit(Result.failure(ApiError(ResponceCodes.NOTHING_FOUND)))
                }
            }
            else -> emit(Result.failure(ApiError(response.resultCode)))
        }
    }
    companion object {
        const val TAG_SEARCH_RESPONSE = "SearchRepositoryImpl"
    }
}
