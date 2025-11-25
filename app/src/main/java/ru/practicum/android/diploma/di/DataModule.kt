package ru.practicum.android.diploma.di

import org.koin.dsl.module

val dataModule = module {
    // Включаем подмодули для сетевого слоя и базы данных
    includes(networkModule, databaseModule)
}
