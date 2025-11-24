package ru.practicum.android.diploma.di

import org.koin.dsl.module

val repositoryModule = module {
    // Репозитории будут добавлены после создания классов
    // Например:
    // 
    // // Поиск и детали вакансий
    // single<VacancyRepository> {
    //     VacancyRepositoryImpl(
    //         api = get(),
    //         vacancyDao = get()
    //     )
    // }
    //
    // // Фильтры
    // single<FilterRepository> {
    //     FilterRepositoryImpl(
    //         filterPrefs = get()
    //     )
    // }
}
