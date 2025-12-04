package ru.practicum.android.diploma.vacancy.domain.models

import ru.practicum.android.diploma.domain.models.Address
import ru.practicum.android.diploma.domain.models.Contacts
import ru.practicum.android.diploma.domain.models.Employer
import ru.practicum.android.diploma.domain.models.Employment
import ru.practicum.android.diploma.domain.models.Experience
import ru.practicum.android.diploma.domain.models.FilterArea
import ru.practicum.android.diploma.domain.models.KeySkill
import ru.practicum.android.diploma.domain.models.Salary
import ru.practicum.android.diploma.domain.models.Schedule

data class VacancyDetailed(
    val address: Address?,
    val url: String,
    val area: FilterArea,
    val contacts: Contacts?,
    val description: String,
    val employer: Employer?,
    val employment: Employment?,
    val experience: Experience?,
    val id: String,
    val keySkills: List<KeySkill>,
    val name: String,
    val salary: Salary?,
    val schedule: Schedule?,
)
