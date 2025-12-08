package ru.practicum.android.diploma.di

import org.koin.dsl.module

val appModule = module {
    includes(
        networkModule,
        storageModule,
        databaseModule,
        favoritesModule,
        vacancyModule,
        searchModule,
        filtersModule
    )
}
