package ru.practicum.android.diploma.vacancy.data

import android.util.Log
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.ApiError
import ru.practicum.android.diploma.data.ResponceCodes
import ru.practicum.android.diploma.data.dto.requests.VacancyByIdRequest
import ru.practicum.android.diploma.data.dto.responses.VacancyByIdResponse
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.search.data.SearchRepositoryImpl.Companion.TAG_SEARCH_RESPONSE
import ru.practicum.android.diploma.vacancy.domain.VacancyRepository
import ru.practicum.android.diploma.vacancy.domain.mappers.toDomain
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailed

class VacancyRepositoryImpl(
    private val networkClient: NetworkClient
): VacancyRepository {
    override suspend fun getVacancyById(id: String): Flow<Result<VacancyDetailed>> = flow {
        val vacancyIdRequest = VacancyByIdRequest(id = id)
        val response = networkClient.getVacancyById(vacancyIdRequest)
        when (response.resultCode) {
            ResponceCodes.ERROR_NO_INTERNET -> emit(Result.failure(ApiError(ResponceCodes.ERROR_NO_INTERNET)))
            ResponceCodes.IO_EXCEPTION -> emit(Result.failure(ApiError(ResponceCodes.IO_EXCEPTION)))
            ResponceCodes.SUCCESS_STATUS -> {
                val vacancyIdResponse = response as? VacancyByIdResponse

                if (vacancyIdResponse != null) {
                    try {
                        val domain = vacancyIdResponse.toDomain()
                        emit(Result.success(domain))
                    } catch (t: IllegalArgumentException) {
                        Log.d(TAG_SEARCH_RESPONSE, "$vacancyIdResponse", t)
                        // Если маппер упал по какой-то причине
                        emit(Result.failure(ApiError(ResponceCodes.MAPPER_EXCEPTION)))
                    } catch (e: JsonSyntaxException) {
                        Log.d(TAG_SEARCH_RESPONSE, "$vacancyIdResponse", e)
                        emit(Result.failure(ApiError(ResponceCodes.MAPPER_EXCEPTION)))
                    }
                } else {
                    emit(Result.failure(ApiError(ResponceCodes.NOTHING_FOUND)))
                }
            }
        }
    }
}
