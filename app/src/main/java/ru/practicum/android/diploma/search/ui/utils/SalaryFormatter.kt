package ru.practicum.android.diploma.search.ui.utils

import android.content.Context
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.search.domain.models.Salary
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object SalaryFormatter {
    private val currencyMap = mapOf(
        "RUR" to "₽",
        "RUB" to "₽",
        "BYR" to "Br",
        "USD" to "$",
        "EUR" to "€",
        "KZT" to "₸",
        "UAH" to "₴",
        "AZN" to "₼",
        "UZS" to "лв",
        "GEL" to "ლ",
        "KGT" to "с"
    )

    @Volatile
    private var cachedLocale: Locale? = null
    @Volatile
    private var cachedFormatter: DecimalFormat? = null

    private fun getFormatter(locale: Locale): DecimalFormat {
        val current = cachedFormatter
        if (cachedLocale == locale && current != null) return current

        synchronized(this) {
            if (cachedLocale == locale && cachedFormatter != null) return cachedFormatter!!

            val symbols = DecimalFormatSymbols(locale).apply {
                groupingSeparator = ' '
            }
            val df = DecimalFormat("#,###", symbols)
            cachedLocale = locale
            cachedFormatter = df
            return df
        }
    }

    fun format(context: Context, salary: Salary?): String {
        if (salary == null) return context.getString(R.string.salary_not_specified)

        val from = salary.from
        val to = salary.to
        val currencyCode = salary.currency ?: ""

        val currencySymbol = currencyMap[currencyCode] ?: currencyCode

        val formatter = getFormatter(Locale.getDefault())

        return when {
            from != null && to != null ->
                context.getString(R.string.salary_from_to, formatter.format(from), formatter.format(to), currencySymbol)
            from != null ->
                context.getString(R.string.salary_from, formatter.format(from), currencySymbol)
            to != null ->
                context.getString(R.string.salary_to, formatter.format(to), currencySymbol)
            else ->
                context.getString(R.string.salary_not_specified)
        }
    }
}
