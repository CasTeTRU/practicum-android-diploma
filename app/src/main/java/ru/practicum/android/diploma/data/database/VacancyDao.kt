package ru.practicum.android.diploma.data.database

import androidx.room.Dao
import kotlinx.coroutines.flow.Flow

@Dao
interface VacancyDao {
    @androidx.room.Query("SELECT * FROM favorite_vacancies ORDER BY id")
    fun getAllFavorites(): Flow<List<VacancyEntity>>

    @androidx.room.Query("SELECT * FROM favorite_vacancies WHERE id = :id")
    suspend fun getFavoriteById(id: String): VacancyEntity?

    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(vacancyEntity: VacancyEntity)

    @androidx.room.Query("DELETE FROM favorite_vacancies WHERE id = :id")
    suspend fun deleteFavorite(id: String)

    @androidx.room.Query("SELECT EXISTS(SELECT 1 FROM favorite_vacancies WHERE id = :id)")
    suspend fun isFavorite(id: String): Boolean
}
