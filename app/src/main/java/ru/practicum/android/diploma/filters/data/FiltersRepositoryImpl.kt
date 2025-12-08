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
import ru.practicum.android.diploma.filters.domain.api.FiltersRepository
import ru.practicum.android.diploma.filters.domain.api.FiltersStorage
import ru.practicum.android.diploma.filters.domain.models.FiltersParameters

class FiltersRepositoryImpl(
    private val networkClient: NetworkClient,
    private val filtersStorage: FiltersStorage
) : FiltersRepository {
    override suspend fun fetchIndustries(): Flow<Result<List<FilterIndustry>>> = flow {
        val response = networkClient.getIndustries()
        when (response.resultCode) {
            ResponseCodes.ERROR_NO_INTERNET -> emit(Result.failure(ApiError(ResponseCodes.ERROR_NO_INTERNET)))
            ResponseCodes.IO_EXCEPTION -> emit(Result.failure(ApiError(ResponseCodes.IO_EXCEPTION)))
            ResponseCodes.SUCCESS_STATUS -> {
                val industriesResponse = response.data

                if (industriesResponse !== null) {
                    try {
                        val domain = industriesResponse.map { it.toDomain() }
                        emit(Result.success(domain))
                    } catch (t: IllegalArgumentException) {
                        Log.d(TAG_FILTERS_RESPONSE, "$industriesResponse", t)
                        // Если маппер упал по какой-то причине
                        emit(Result.failure(ApiError(ResponseCodes.MAPPER_EXCEPTION)))
                    } catch (e: JsonSyntaxException) {
                        Log.d(TAG_FILTERS_RESPONSE, "$industriesResponse", e)
                        emit(Result.failure(ApiError(ResponseCodes.MAPPER_EXCEPTION)))
                    }
                } else {
                    emit(Result.failure(ApiError(ResponseCodes.NOTHING_FOUND)))
                }
            }
            else -> emit(Result.failure(ApiError(response.resultCode)))
        }
    }

    override suspend fun getSavedFilter(): FiltersParameters = filtersStorage.getFilterSettings()

    override suspend fun saveFilter(settings: FiltersParameters) = filtersStorage.saveFilterSettings(settings)

    override suspend fun clearFilter() = filtersStorage.clearFilterSettings()

    override suspend fun getIndustry() = filtersStorage.getIndustry()

    override suspend fun saveIndustry(industry: FilterIndustry) = filtersStorage.saveIndustry(industry)

    override suspend fun clearIndustry() = filtersStorage.clearIndustry()

    override suspend fun restorePreviousState() = filtersStorage.restorePreviousState()

    companion object {
        const val TAG_FILTERS_RESPONSE = "FiltersRepositoryImpl"
    }
}
