package ru.practicum.android.diploma.vacancy.domain.impl

import ru.practicum.android.diploma.vacancy.domain.VacancyInteractor
import ru.practicum.android.diploma.vacancy.domain.VacancyRepository
import ru.practicum.android.diploma.vacancy.domain.mappers.VacancyMapper
import ru.practicum.android.diploma.vacancy.domain.models.VacancyShow

// TODO: Implement FavoriteRepository and uncomment its usage here and in DI.
// import ru.practicum.android.diploma.favorite.domain.FavoriteRepository

class VacancyInteractorImpl(
    private val vacancyRepository: VacancyRepository,
    private val vacancyMapper: VacancyMapper,
    // TODO: Uncomment when FavoriteRepository is ready.
    // private val favoriteRepository: FavoriteRepository
) : VacancyInteractor {

    override suspend fun getVacancyById(id: String): Result<VacancyShow> {
        // We get the result from the data layer repository
        val result = vacancyRepository.getVacancyById(id)

        // Check if the result is successful
        if (result.isSuccess) {
            // The request was successful, get the data. It might be null.
            val vacancyDomain = result.getOrNull()

            return if (vacancyDomain != null) {
                // Map the domain model to a presentation model (VacancyShow)
                val vacancyShow = vacancyMapper.mapToShow(vacancyDomain)

                // TODO: Implement favorite checking logic.
                // For now, we assume the vacancy is not a favorite.
                // val isFavorite = favoriteRepository.isFavorite(vacancyShow.id)
                // val finalVacancy = vacancyShow.copy(isFavorite = isFavorite)

                // Return a new successful Result with the mapped data
                Result.success(vacancyShow)
            } else {
                // This case means the server responded but found no such vacancy.
                // We create a failure Result with an exception.
                Result.failure(Exception("Vacancy not found"))
            }
        } else {
            // An error occurred (e.g., no internet, server error).
            // TODO: Implement offline logic (loading from favorites) when FavoriteRepository is ready.

            // Pass the original error through.
            // We create a new failure to ensure the type matches Result<VacancyShow>.
            return Result.failure(result.exceptionOrNull() ?: Exception("An unknown error occurred"))
        }
    }
}
