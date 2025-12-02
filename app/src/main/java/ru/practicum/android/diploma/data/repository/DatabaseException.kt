package ru.practicum.android.diploma.data.repository

/**
 * Исключение для ошибок работы с базой данных
 */
class DatabaseException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)

