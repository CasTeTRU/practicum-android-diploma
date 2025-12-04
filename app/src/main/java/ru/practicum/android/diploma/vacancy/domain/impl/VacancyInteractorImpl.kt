package ru.practicum.android.diploma.vacancy.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.vacancy.domain.VacancyInteractor
import ru.practicum.android.diploma.vacancy.domain.VacancyRepository
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailed

class VacancyInteractorImpl(
    private val vacancyRepository: VacancyRepository,
    // private val favoriteRepository: FavoriteRepository
) : VacancyInteractor {

    override suspend fun getVacancyById(id: String): Flow<Result<VacancyDetailed>> {
        return vacancyRepository.getVacancyById(id)
    }
}
