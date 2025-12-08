package ru.practicum.android.diploma.filters.data

import android.util.Log
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.ApiError
import ru.practicum.android.diploma.data.ResponceCodes
import ru.practicum.android.diploma.data.dto.responses.IndustriesResponse
import ru.practicum.android.diploma.data.mapper.toDomain
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.filters.domain.api.IndustryRepository

class IndustryRepositoryImpl(
    private val networkClient: NetworkClient
) : IndustryRepository {
    override suspend fun getIndustries(): Flow<Result<List<FilterIndustry>>> = flow {
        val response = networkClient.getIndustries()

        when (response.resultCode) {
            ResponceCodes.ERROR_NO_INTERNET -> emit(Result.failure(ApiError(ResponceCodes.ERROR_NO_INTERNET)))
            ResponceCodes.IO_EXCEPTION -> emit(Result.failure(ApiError(ResponceCodes.IO_EXCEPTION)))
            ResponceCodes.SUCCESS_STATUS -> {
                val industriesResponse = response as? IndustriesResponse

                if (industriesResponse?.industries != null) {
                    try {
                        val domainList = industriesResponse.industries.map { it.toDomain() }
                        emit(Result.success(domainList))
                    } catch (t: IllegalArgumentException) {
                        Log.d(TAG, "$industriesResponse", t)
                        emit(Result.failure(ApiError(ResponceCodes.MAPPER_EXCEPTION)))
                    } catch (e: JsonSyntaxException) {
                        Log.d(TAG, "$industriesResponse", e)
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
        const val TAG = "IndustryRepositoryImpl"
    }
}
