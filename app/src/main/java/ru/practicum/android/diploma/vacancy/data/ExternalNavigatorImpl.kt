package ru.practicum.android.diploma.vacancy.data

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.net.toUri
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.vacancy.domain.ExternalNavigator

class ExternalNavigatorImpl(private val context: Context) : ExternalNavigator {
    override fun emailTo(email: String) {
        val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:$email".toUri()
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(emailIntent)
        } catch (e: ActivityNotFoundException) {
            Log.e("ExternalNavigatorImpl", "No activity found to handle email intent", e)
        }
    }

    override fun callTo(phoneNumber: String) {
        val callIntent = Intent(Intent.ACTION_DIAL).apply {
            data = "tel:$phoneNumber".toUri()
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            context.startActivity(callIntent)
        } catch (e: ActivityNotFoundException) {
            Log.e("ExternalNavigatorImpl", "No activity found to handle dial intent", e)
        }
    }

    override fun shareLink(alternateUrl: String) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, alternateUrl)
            type = "text/plain"
        }

        try {
            val chooser = Intent.createChooser(shareIntent, context.getString(R.string.share))
            chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(chooser)
        } catch (e: ActivityNotFoundException) {
            Log.e("ExternalNavigatorImpl", "No activity found to handle sharing intent", e)
        }
    }
}
