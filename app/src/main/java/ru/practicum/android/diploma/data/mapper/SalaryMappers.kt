package ru.practicum.android.diploma.data.mapper

import ru.practicum.android.diploma.data.dto.responses.SalaryDTO
import ru.practicum.android.diploma.domain.models.Salary

fun SalaryDTO.toDomain(): Salary = Salary(
    from = from,
    to = to,
    currency = currency
)
