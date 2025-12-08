package ru.practicum.android.diploma.search.data

import android.util.Log
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.ApiError
import ru.practicum.android.diploma.data.ResponseCodes
import ru.practicum.android.diploma.data.dto.requests.VacanciesSearchRequest
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
            industry = filters?.industry?.id,
            page = page,
            onlyWithSalary = filters?.onlyWithSalary ?: false
        )
        val response = networkClient.findVacancies(vacancySearchRequest)

        when (response.resultCode) {
            ResponseCodes.ERROR_NO_INTERNET -> emit(Result.failure(ApiError(ResponseCodes.ERROR_NO_INTERNET)))
            ResponseCodes.IO_EXCEPTION -> emit(Result.failure(ApiError(ResponseCodes.IO_EXCEPTION)))
            ResponseCodes.SUCCESS_STATUS -> {
                val vacancyResponse = response.data

                if (vacancyResponse?.items !== null) {
                    try {
                        val domain = vacancyResponse.toDomain()
                        emit(Result.success(domain))
                    } catch (t: IllegalArgumentException) {
                        Log.e(TAG, "Failed to map vacancy response", t)
                        emit(Result.failure(ApiError(ResponseCodes.MAPPER_EXCEPTION)))
                    } catch (e: JsonSyntaxException) {
                        Log.e(TAG, "Failed to parse vacancy response JSON", e)
                        emit(Result.failure(ApiError(ResponseCodes.MAPPER_EXCEPTION)))
                    }
                } else {
                    emit(Result.failure(ApiError(ResponseCodes.NOTHING_FOUND)))
                }
            }
            else -> emit(Result.failure(ApiError(response.resultCode)))
        }
    }

    companion object {
        private const val TAG = "SearchRepositoryImpl"
    }
}
