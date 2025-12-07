package ru.practicum.android.diploma.domain.util.impl

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import ru.practicum.android.diploma.domain.util.ResourcesProviderInteractor

class ResourcesProviderInteractorImpl(private val context: Context) : ResourcesProviderInteractor {
    override fun getString(stringResId: Int): String {
        return context.getString(stringResId)
    }

    override fun getDrawable(drawableResId: Int): Drawable? {
        return ContextCompat.getDrawable(context, drawableResId)
    }
}
