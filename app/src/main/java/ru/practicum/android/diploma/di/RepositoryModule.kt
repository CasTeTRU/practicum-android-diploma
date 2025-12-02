package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.repository.FavoriteRepositoryImpl
import ru.practicum.android.diploma.favorites.domain.api.FavoriteRepository

val repositoryModule = module {
    single<FavoriteRepository> {
        FavoriteRepositoryImpl(
            vacancyDao = get()
        )
    }
}
