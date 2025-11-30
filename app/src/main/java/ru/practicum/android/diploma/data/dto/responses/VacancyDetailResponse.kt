package ru.practicum.android.diploma.data.dto.responses

import ru.practicum.android.diploma.data.dto.NetworkResponse

data class VacancyDetailResponse(
    val id: String?,
    val name: String?,
    val description: String?,
    val salary: SalaryDTO?,
    val address: AddressDTO?,
    val experience: ExperienceDTO?,
    val schedule: ScheduleDTO?,
    val employment: EmploymentDTO?,
    val contacts: ContactsDTO?,
    val employer: EmployerDTO?,
    val area: FilterAreaDTO?,
    val skills: List<String>?,
    val url: String?,
    val industry: FilterIndustryDTO?,
) : NetworkResponse()
