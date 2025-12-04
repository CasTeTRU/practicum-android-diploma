package ru.practicum.android.diploma.di

import org.koin.dsl.module

val appModule = module {
    includes(
        networkModule,
        storageModule,
        databaseModule,
        repositoryModule,
        viewModelModule,
        searchModule,
        vacancyModule
    )
}
