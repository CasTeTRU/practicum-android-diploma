package ru.practicum.android.diploma.data.dto.responses

import ru.practicum.android.diploma.data.dto.NetworkResponse

class VacancyByIdResponse(
    val id: String,
    val name: String,
    val description: String,
    val salary: SalaryDTO? = null,
    val address: AddressDTO? = null,
    val experience: ExperienceDTO? = null,
    val schedule: ScheduleDTO? = null,
    val employment: EmploymentDTO? = null,
    val contacts: ContactsDTO? = null,
    val employer: EmployerDTO? = null,
    val area: FilterAreaDTO? = null,
    val skills: List<String>? = null,
    val url: String,
    val industry: FilterIndustryDTO? = null,
) : NetworkResponse()
