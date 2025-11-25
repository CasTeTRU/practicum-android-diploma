package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.BuildConfig

object NetworkConfig {
    val BASE_URL = BuildConfig.BASE_URL
    const val CONNECT_TIMEOUT_SECONDS = 30L
    const val READ_TIMEOUT_SECONDS = 30L
    const val WRITE_TIMEOUT_SECONDS = 30L
}
