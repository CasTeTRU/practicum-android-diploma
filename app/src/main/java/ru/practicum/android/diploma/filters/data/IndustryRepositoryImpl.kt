package ru.practicum.android.diploma.filters.data

import android.util.Log
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.ApiError
import ru.practicum.android.diploma.data.ResponseCodes
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
            ResponseCodes.ERROR_NO_INTERNET -> emit(Result.failure(ApiError(ResponseCodes.ERROR_NO_INTERNET)))
            ResponseCodes.IO_EXCEPTION -> emit(Result.failure(ApiError(ResponseCodes.IO_EXCEPTION)))
            ResponseCodes.SUCCESS_STATUS -> {
                val industriesList = response.data

                if (industriesList != null) {
                    try {
                        val domainList = industriesList.map { it.toDomain() }
                        emit(Result.success(domainList))
                    } catch (t: IllegalArgumentException) {
                        Log.d(TAG, "$industriesList", t)
                        emit(Result.failure(ApiError(ResponseCodes.MAPPER_EXCEPTION)))
                    } catch (e: JsonSyntaxException) {
                        Log.d(TAG, "$industriesList", e)
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
        const val TAG = "IndustryRepositoryImpl"
    }
}
