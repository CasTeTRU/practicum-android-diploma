package ru.practicum.android.diploma.vacancy.domain.mappers

import ru.practicum.android.diploma.vacancy.domain.models.Vacancy
import ru.practicum.android.diploma.vacancy.domain.models.VacancyShow

// Until the SalaryFormatter is ready, we don't accept it in the constructor.
// When it is ready, you add it here: class VacancyMapper(private val salaryFormatter: SalaryFormatter)

class VacancyMapper {

    fun mapToShow(domain: Vacancy): VacancyShow {
        return VacancyShow(
            id = domain.id,
            name = domain.name,
            description = domain.description,
            experience = domain.experience,
            schedule = domain.schedule,
            employment = domain.employment,
            contacts = domain.contacts,
            employerName = domain.employer.name,
            urlLogo = domain.employer.logo,
            isFavorite = false, // По умолчанию пока false
            urlLink = domain.linkUrl,
            address = domain.address.raw.ifEmpty { domain.address.city },

            // PLUG for formatting required skills
            // When skillsFormatter is ready, we will replace this line
            skills = domain.skills?.joinToString(separator = ", "), // Simple string concatenation

            // PLUG for salary formatting
            // Once SalaryFormatter is ready, we can use it
            salary = "Зарплата не указана", // Temporary placeholder string
        )
    }
}
