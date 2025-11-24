package ru.practicum.android.diploma.di

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.network.ApiService
import java.util.concurrent.TimeUnit

val networkModule = module {
    // OkHttpClient с LoggingInterceptor
    single {
        OkHttpClient.Builder().apply {
            connectTimeout(30, TimeUnit.SECONDS)
            readTimeout(30, TimeUnit.SECONDS)
            writeTimeout(30, TimeUnit.SECONDS)
            
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
    
    // GsonConverterFactory
    single { GsonConverterFactory.create() }
    
    // Retrofit
    single {
        Retrofit.Builder()
            .baseUrl("https://practicum-diploma-8bc38133faba.herokuapp.com/")
            .client(get<OkHttpClient>())
            .addConverterFactory(get<GsonConverterFactory>())
            .build()
    }
    
    // API Service
    single<ApiService> {
        get<Retrofit>().create(ApiService::class.java)
    }
}

