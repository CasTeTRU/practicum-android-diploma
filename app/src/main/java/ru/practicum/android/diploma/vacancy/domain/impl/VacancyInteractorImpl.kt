package ru.practicum.android.diploma.vacancy.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.vacancy.domain.ExternalNavigator
import ru.practicum.android.diploma.vacancy.domain.VacancyInteractor
import ru.practicum.android.diploma.vacancy.domain.VacancyRepository
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailed

class VacancyInteractorImpl(
    private val navigation: ExternalNavigator,
    private val vacancyRepository: VacancyRepository,
    // private val favoriteRepository: FavoriteRepository
) : VacancyInteractor {
    override fun shareLink(alternateUrl: String) {
        navigation.shareLink(alternateUrl)
    }

    override fun emailTo(email: String) {
        navigation.emailTo(email)
    }

    override fun callTo(phoneNumber: String) {
        navigation.callTo(phoneNumber)
    }

    override suspend fun getVacancyById(id: String): Flow<Result<VacancyDetailed>> {
        return vacancyRepository.getVacancyById(id)
    }
}
