package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.domain.util.ResourcesProviderInteractor
import ru.practicum.android.diploma.domain.util.impl.ResourcesProviderInteractorImpl
import ru.practicum.android.diploma.vacancy.data.ExternalNavigatorImpl
import ru.practicum.android.diploma.vacancy.data.VacancyRepositoryImpl
import ru.practicum.android.diploma.vacancy.domain.ExternalNavigator
import ru.practicum.android.diploma.vacancy.domain.VacancyInteractor
import ru.practicum.android.diploma.vacancy.domain.VacancyRepository
import ru.practicum.android.diploma.vacancy.domain.impl.VacancyInteractorImpl
import ru.practicum.android.diploma.vacancy.presentation.VacancyViewModel

val vacancyModule = module {
    single<ExternalNavigator> { ExternalNavigatorImpl(get()) }
    single<ResourcesProviderInteractor> { ResourcesProviderInteractorImpl(get()) }

    single<VacancyRepository> { VacancyRepositoryImpl(get()) }

    single<VacancyInteractor> { VacancyInteractorImpl(get(), get()) }

    viewModel { VacancyViewModel(get(), get(), get()) }
}
