package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.filters.data.FilterRepositoryImpl
import ru.practicum.android.diploma.filters.data.FilterStorage
import ru.practicum.android.diploma.filters.data.IndustryRepositoryImpl
import ru.practicum.android.diploma.filters.domain.api.FilterInteractor
import ru.practicum.android.diploma.filters.domain.api.FilterRepository
import ru.practicum.android.diploma.filters.domain.api.IndustryInteractor
import ru.practicum.android.diploma.filters.domain.api.IndustryRepository
import ru.practicum.android.diploma.filters.domain.impl.FilterInteractorImpl
import ru.practicum.android.diploma.filters.domain.impl.IndustryInteractorImpl
import ru.practicum.android.diploma.filters.presentation.FilterIndustryViewModel
import ru.practicum.android.diploma.filters.presentation.FiltersViewModel

val filtersModule = module {
    single { FilterStorage(get(), get()) }
    
    single<FilterRepository> { FilterRepositoryImpl(get()) }
    
    single<FilterInteractor> { FilterInteractorImpl(get()) }

    single<IndustryRepository> { IndustryRepositoryImpl(get()) }

    single<IndustryInteractor> { IndustryInteractorImpl(get()) }

    viewModel { FilterIndustryViewModel(get(), get()) }
    
    viewModel { FiltersViewModel(get(), get()) }
}
