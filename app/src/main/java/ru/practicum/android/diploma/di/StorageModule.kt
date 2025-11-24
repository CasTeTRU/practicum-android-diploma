package ru.practicum.android.diploma.di

import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val storageModule = module {
    // Gson для сериализации/десериализации фильтров
    single { Gson() }
    
    // FilterPreferences будет добавлен после создания класса
    // single<FilterPreferences> {
    //     FilterPreferences(
    //         context = androidContext(),
    //         gson = get()
    //     )
    // }
}
