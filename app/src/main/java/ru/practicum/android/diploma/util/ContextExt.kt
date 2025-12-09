package ru.practicum.android.diploma.util

import android.content.Context
import android.text.Html
import android.util.TypedValue

fun Context.toPx(dp: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp,
        resources.displayMetrics
    ).toInt()
}

fun formatDescription(context: Context, description: String): CharSequence {
    return Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT)
}
