package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    // ViewModels будут добавлены после создания классов
    // Например:
    //
    // // Основные экраны
    // viewModel { SearchViewModel(get(), get(), get()) }
    // viewModel { FavoritesViewModel(get()) }
    // viewModel { FilterViewModel(get(), get()) }
    // viewModel { RegionListViewModel(get()) }
    // viewModel { IndustryListViewModel(get()) }
    //
    // // Детали вакансии (с параметром)
    // viewModel { (vacancyId: String) ->
    //     VacancyDetailViewModel(vacancyId, get(), get(), get())
    // }
}
