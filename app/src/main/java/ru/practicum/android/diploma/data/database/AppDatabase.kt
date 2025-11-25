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
    // TODO: Добавить DAO интерфейсы
    // Пример: abstract fun vacancyDao(): VacancyDao
    // Пример: abstract fun favoriteDao(): FavoriteDao
}
