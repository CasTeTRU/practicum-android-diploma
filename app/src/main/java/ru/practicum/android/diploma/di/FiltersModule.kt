package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.filters.data.FilterRepositoryImpl
import ru.practicum.android.diploma.filters.data.FilterStorage
import ru.practicum.android.diploma.filters.data.FiltersRepositoryImpl
import ru.practicum.android.diploma.filters.data.FiltersStorageImpl
import ru.practicum.android.diploma.filters.data.IndustryRepositoryImpl
import ru.practicum.android.diploma.filters.domain.api.FilterInteractor
import ru.practicum.android.diploma.filters.domain.api.FilterRepository
import ru.practicum.android.diploma.filters.domain.api.FiltersInteractor
import ru.practicum.android.diploma.filters.domain.api.FiltersRepository
import ru.practicum.android.diploma.filters.domain.api.FiltersStorage
import ru.practicum.android.diploma.filters.domain.api.IndustryInteractor
import ru.practicum.android.diploma.filters.domain.api.IndustryRepository
import ru.practicum.android.diploma.filters.domain.impl.FilterInteractorImpl
import ru.practicum.android.diploma.filters.domain.impl.FiltersInteractorImpl
import ru.practicum.android.diploma.filters.domain.impl.IndustryInteractorImpl
import ru.practicum.android.diploma.filters.presentation.FilterIndustryViewModel
import ru.practicum.android.diploma.filters.presentation.FiltersViewModel
import ru.practicum.android.diploma.filters.presentation.IndustryViewModel

val filtersModule = module {
    single { FilterStorage(get(), get()) }
    single<FilterRepository> { FilterRepositoryImpl(get()) }
    single<FilterInteractor> { FilterInteractorImpl(get()) }
    single<IndustryRepository> { IndustryRepositoryImpl(get()) }
    single<IndustryInteractor> { IndustryInteractorImpl(get()) }
    viewModel { FilterIndustryViewModel(get<IndustryInteractor>(), get<FiltersInteractor>()) }

    single<FiltersStorage> { FiltersStorageImpl(get(), get()) }

    single<FiltersRepository> { FiltersRepositoryImpl(get(), get()) }

    single<FiltersInteractor> { FiltersInteractorImpl(get()) }

    viewModel { FiltersViewModel(get()) }
    viewModel { IndustryViewModel(get()) }
}
