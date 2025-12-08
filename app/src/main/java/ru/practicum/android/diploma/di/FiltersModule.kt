package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.filters.data.FiltersRepositoryImpl
import ru.practicum.android.diploma.filters.data.FiltersStorageImpl
import ru.practicum.android.diploma.filters.domain.api.FiltersInteractor
import ru.practicum.android.diploma.filters.domain.api.FiltersRepository
import ru.practicum.android.diploma.filters.domain.api.FiltersStorage
import ru.practicum.android.diploma.filters.domain.impl.FiltersInteractorImpl
import ru.practicum.android.diploma.filters.presentation.FiltersViewModel
import ru.practicum.android.diploma.filters.presentation.IndustryViewModel

val filtersModule = module {

    single<FiltersStorage> { FiltersStorageImpl(get(),get()) }

    single<FiltersRepository> { FiltersRepositoryImpl(get(), get()) }

    single<FiltersStorage> { FiltersStorageImpl(get(), get()) }

    single<FiltersInteractor> { FiltersInteractorImpl(get()) }

    viewModel { FiltersViewModel(get()) }
    viewModel { IndustryViewModel(get()) }
}
