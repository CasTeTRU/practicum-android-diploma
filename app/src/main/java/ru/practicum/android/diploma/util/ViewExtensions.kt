package ru.practicum.android.diploma.util

import android.view.View

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun View.showIf(condition: Boolean) {
    visibility = if (condition) {
        View.VISIBLE
    } else {
        View.GONE
    }
}
