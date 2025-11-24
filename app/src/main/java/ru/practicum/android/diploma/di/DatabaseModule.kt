package ru.practicum.android.diploma.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import ru.practicum.android.diploma.data.database.AppDatabase

val databaseModule = module {
    // Room Database
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "diploma_database"
        )
            .fallbackToDestructiveMigration() // Внимание: удаляет данные при изменении схемы
            .build()
    }
    
    // DAO интерфейсы будут добавлены здесь после создания
    // Например:
    // single { get<AppDatabase>().vacancyDao() }
}

