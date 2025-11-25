package ru.practicum.android.diploma.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.network.ApiService
import ru.practicum.android.diploma.data.network.NetworkConfig
import java.util.concurrent.TimeUnit

val networkModule = module {
    // OkHttpClient с LoggingInterceptor
    single {
        OkHttpClient.Builder().apply {
            connectTimeout(NetworkConfig.CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            readTimeout(NetworkConfig.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            writeTimeout(NetworkConfig.WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            // Добавляем LoggingInterceptor для отладки
            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
                addInterceptor(loggingInterceptor)
            }
            // Добавляем интерцептор для авторизации
            addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${BuildConfig.API_ACCESS_TOKEN}")
                    .build()
                chain.proceed(request)
            }
        }.build()
    }

    single<Gson> {
        GsonBuilder().create()
    }
    
    // Retrofit
    single {
        Retrofit.Builder()
            .baseUrl(NetworkConfig.BASE_URL)
            .client(get<OkHttpClient>())
            .addConverterFactory(get<GsonConverterFactory>())
            .build()
    }
    
    // API Service
    single<ApiService> {
        get<Retrofit>().create(ApiService::class.java)
    }
}
