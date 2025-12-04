package ru.practicum.android.diploma.domain.util

import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

interface ResourcesProviderInteractor {
    fun getString(@StringRes stringResId: Int): String
    fun getDrawable(@DrawableRes drawableResId: Int): Drawable?
}
