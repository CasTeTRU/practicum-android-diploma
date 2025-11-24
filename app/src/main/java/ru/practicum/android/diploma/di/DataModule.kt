package ru.practicum.android.diploma.di

import org.koin.dsl.module

val dataModule = module {
    // Включаем подмодули для сетевого слоя и базы данных
    includes(networkModule, databaseModule)
    
    // Здесь будут определены зависимости для data слоя
    // Например: Repository реализации, DataSource классы и т.д.
}

