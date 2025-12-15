package ru.practicum.android.diploma.domain.sharing.impl

import ru.practicum.android.diploma.domain.sharing.SharingInteractor
import ru.practicum.android.diploma.domain.sharing.api.SharingRepository

class SharingInteractorImpl(
    private val sharingRepository: SharingRepository
) : SharingInteractor {
    override fun shareVacancy(url: String) {
        sharingRepository.shareVacancy(url)
    }

    override fun sendOnEmail(email: String) {
        sharingRepository.sendOnEmail(email)
    }

    override fun call(number: String) {
        sharingRepository.call(number)
    }
}
