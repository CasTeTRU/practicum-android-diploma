package ru.practicum.android.diploma.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        VacancyEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    // Здесь будут добавлены DAO интерфейсы
    // Например:
    // abstract fun vacancyDao(): VacancyDao
    // abstract fun favoriteDao(): FavoriteDao
}


