package ru.practicum.android.diploma.data.sharing.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import ru.practicum.android.diploma.domain.sharing.api.SharingRepository

class SharingRepositoryImpl(private val context: Context) : SharingRepository {

    override fun shareVacancy(url: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, url)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            // В идеале здесь нужно обработать ошибку, например, показать Toast
        }
    }

    override fun sendOnEmail(email: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            // В идеале здесь нужно обработать ошибку, например, показать Toast
        }
    }

    override fun call(number: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$number")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            // В идеале здесь нужно обработать ошибку, например, показать Toast
        }
    }
}
