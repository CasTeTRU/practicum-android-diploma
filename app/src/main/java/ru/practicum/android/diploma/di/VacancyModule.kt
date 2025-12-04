package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.vacancy.data.VacancyRepositoryImpl
import ru.practicum.android.diploma.vacancy.domain.VacancyInteractor
import ru.practicum.android.diploma.vacancy.domain.VacancyRepository
import ru.practicum.android.diploma.vacancy.domain.impl.VacancyInteractorImpl
import ru.practicum.android.diploma.vacancy.presentation.VacancyViewModel

val vacancyModule = module {

    single<VacancyRepository> { VacancyRepositoryImpl(get()) }

    single<VacancyInteractor> { VacancyInteractorImpl(get()) }

    viewModel { VacancyViewModel(get()) }
}

