package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.domain.api.FavoriteInteractor
import ru.practicum.android.diploma.favorites.domain.impl.FavoriteInteractorImpl

val domainModule = module {
    single<FavoriteInteractor> {
        FavoriteInteractorImpl(
            favoriteRepository = get()
        )
    }
}
