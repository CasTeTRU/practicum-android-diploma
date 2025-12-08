package ru.practicum.android.diploma.data.network

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.data.ResponseCodes
import ru.practicum.android.diploma.data.dto.NetworkResponse
import ru.practicum.android.diploma.data.dto.requests.VacanciesSearchRequest
import ru.practicum.android.diploma.data.dto.requests.VacancyByIdRequest
import ru.practicum.android.diploma.data.dto.responses.VacanciesSearchResponse
import ru.practicum.android.diploma.util.NetworkManager
import java.io.IOException

class RetrofitNetworkClient(
    private val apiService: ApiService,
    private val networkManager: NetworkManager
) : NetworkClient {
    private suspend fun <T> safeApiCall(apiCall: suspend () -> T): NetworkResponse<T> {
        if (!networkManager.isConnected()) {
            return NetworkResponse<T>(resultCode = ResponseCodes.ERROR_NO_INTERNET)
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = apiCall()
                NetworkResponse(
                    data = response,
                    resultCode = ResponseCodes.SUCCESS_STATUS
                )
            } catch (e: HttpException) {
                Log.e(TAG, e.stackTraceToString())
                NetworkResponse<T>(resultCode = e.code())
            } catch (e: IOException) {
                Log.e(TAG, e.stackTraceToString())
                NetworkResponse<T>(resultCode = ResponseCodes.IO_EXCEPTION)
            }
        }
    }

    override suspend fun findVacancies(dto: VacanciesSearchRequest): NetworkResponse<VacanciesSearchResponse> =
        safeApiCall { apiService.findVacancies(
            dto.query,
            dto.area,
            dto.salary,
            dto.industry,
            dto.page,
            dto.onlyWithSalary
        ) }

    override suspend fun getVacancyById(dto: VacancyByIdRequest) =
        safeApiCall { apiService.getVacancyById(dto.id) }

    override suspend fun getAreas() =
        safeApiCall { apiService.getAreas() }

    override suspend fun getIndustries() =
        safeApiCall { apiService.getIndustries() }

    companion object {
        const val TAG = "RETROFIT_NETWORK_CLIENT"
    }
}
