package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.data.FavoriteRepositoryImpl
import ru.practicum.android.diploma.favorites.domain.api.FavoriteInteractor
import ru.practicum.android.diploma.favorites.domain.api.FavoriteRepository
import ru.practicum.android.diploma.favorites.domain.impl.FavoriteInteractorImpl
import ru.practicum.android.diploma.favorites.presentation.FavoritesViewModel

val favoritesModule = module {
    single<FavoriteRepository> {
        FavoriteRepositoryImpl(get())
    }

    single<FavoriteInteractor> {
        FavoriteInteractorImpl(get())
    }

    viewModel { FavoritesViewModel(get()) }
}
