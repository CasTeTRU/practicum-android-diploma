package ru.practicum.android.diploma.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.practicum.android.diploma.data.dto.responses.AddressDTO
import ru.practicum.android.diploma.data.dto.responses.ContactsDTO
import ru.practicum.android.diploma.data.dto.responses.EmploymentDTO
import ru.practicum.android.diploma.data.dto.responses.EmployerDTO
import ru.practicum.android.diploma.data.dto.responses.ExperienceDTO
import ru.practicum.android.diploma.data.dto.responses.FilterAreaDTO
import ru.practicum.android.diploma.data.dto.responses.FilterIndustryDTO
import ru.practicum.android.diploma.data.dto.responses.SalaryDTO
import ru.practicum.android.diploma.data.dto.responses.ScheduleDTO

class VacancyTypeConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromSalaryDTO(salary: SalaryDTO?): String? {
        return salary?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toSalaryDTO(salaryJson: String?): SalaryDTO? {
        return salaryJson?.let {
            gson.fromJson(it, SalaryDTO::class.java)
        }
    }

    @TypeConverter
    fun fromAddressDTO(address: AddressDTO?): String? {
        return address?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toAddressDTO(addressJson: String?): AddressDTO? {
        return addressJson?.let {
            gson.fromJson(it, AddressDTO::class.java)
        }
    }

    @TypeConverter
    fun fromExperienceDTO(experience: ExperienceDTO?): String? {
        return experience?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toExperienceDTO(experienceJson: String?): ExperienceDTO? {
        return experienceJson?.let {
            gson.fromJson(it, ExperienceDTO::class.java)
        }
    }

    @TypeConverter
    fun fromScheduleDTO(schedule: ScheduleDTO?): String? {
        return schedule?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toScheduleDTO(scheduleJson: String?): ScheduleDTO? {
        return scheduleJson?.let {
            gson.fromJson(it, ScheduleDTO::class.java)
        }
    }

    @TypeConverter
    fun fromEmploymentDTO(employment: EmploymentDTO?): String? {
        return employment?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toEmploymentDTO(employmentJson: String?): EmploymentDTO? {
        return employmentJson?.let {
            gson.fromJson(it, EmploymentDTO::class.java)
        }
    }

    @TypeConverter
    fun fromContactsDTO(contacts: ContactsDTO?): String? {
        return contacts?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toContactsDTO(contactsJson: String?): ContactsDTO? {
        return contactsJson?.let {
            gson.fromJson(it, ContactsDTO::class.java)
        }
    }

    @TypeConverter
    fun fromEmployerDTO(employer: EmployerDTO): String {
        return gson.toJson(employer)
    }

    @TypeConverter
    fun toEmployerDTO(employerJson: String): EmployerDTO {
        return gson.fromJson(employerJson, EmployerDTO::class.java)
    }

    @TypeConverter
    fun fromFilterAreaDTO(area: FilterAreaDTO): String {
        return gson.toJson(area)
    }

    @TypeConverter
    fun toFilterAreaDTO(areaJson: String): FilterAreaDTO {
        return gson.fromJson(areaJson, FilterAreaDTO::class.java)
    }

    @TypeConverter
    fun fromFilterIndustryDTO(industry: FilterIndustryDTO): String {
        return gson.toJson(industry)
    }

    @TypeConverter
    fun toFilterIndustryDTO(industryJson: String): FilterIndustryDTO {
        return gson.fromJson(industryJson, FilterIndustryDTO::class.java)
    }

    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toStringList(listJson: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(listJson, listType)
    }
}
