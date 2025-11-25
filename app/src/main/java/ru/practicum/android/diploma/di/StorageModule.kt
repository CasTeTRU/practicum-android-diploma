package ru.practicum.android.diploma.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val storageModule = module {
    // Gson для сериализации/десериализации фильтров
    single { Gson() }
    // SharedPreferences для фильтров
    single<SharedPreferences> {
        androidContext().getSharedPreferences(
            "search_filters",
            Context.MODE_PRIVATE
        )
    }
}
