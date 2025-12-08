package ru.practicum.android.diploma.data.dto.responses

class VacancyDetailDTO(
    val id: String,
    val name: String,
    val description: String,
    val salary: SalaryDTO?,
    val address: AddressDTO?,
    val experience: ExperienceDTO?,
    val schedule: ScheduleDTO?,
    val employment: EmploymentDTO?,
    val contacts: ContactsDTO?,
    val employer: EmployerDTO,
    val area: FilterAreaDTO,
    val skills: List<String>,
    val url: String,
    val industry: FilterIndustryDTO,
)
