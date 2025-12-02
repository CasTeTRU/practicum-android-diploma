package ru.practicum.android.diploma.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.practicum.android.diploma.data.dto.responses.AddressDTO
import ru.practicum.android.diploma.data.dto.responses.ContactsDTO
import ru.practicum.android.diploma.data.dto.responses.EmploymentDTO
import ru.practicum.android.diploma.data.dto.responses.EmployerDTO
import ru.practicum.android.diploma.data.dto.responses.ExperienceDTO
import ru.practicum.android.diploma.data.dto.responses.FilterAreaDTO
import ru.practicum.android.diploma.data.dto.responses.FilterIndustryDTO
import ru.practicum.android.diploma.data.dto.responses.SalaryDTO
import ru.practicum.android.diploma.data.dto.responses.ScheduleDTO

@Entity(tableName = "favorite_vacancies")
@TypeConverters(VacancyTypeConverters::class)
data class VacancyEntity(
    @PrimaryKey
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
    val industry: FilterIndustryDTO
)
