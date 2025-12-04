package ru.practicum.android.diploma.domain.sharing.impl

import android.content.Intent
import android.net.Uri
import ru.practicum.android.diploma.domain.sharing.SharingInteractor
import ru.practicum.android.diploma.domain.sharing.models.SharingIntent

class SharingInteractorImpl : SharingInteractor {
    override fun shareVacancy(url: String, vacancyName: String): SharingIntent {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, url)
            putExtra(Intent.EXTRA_SUBJECT, vacancyName)
        }
        return SharingIntent(intent, "Не удалось поделиться вакансией")
    }

    override fun sendOnEmail(email: String): SharingIntent {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email")
        }
        return SharingIntent(intent, "Не удалось открыть почтовый клиент")
    }

    override fun call(number: String): SharingIntent {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$number")
        }
        return SharingIntent(intent, "Не удалось совершить вызов")
    }
}
