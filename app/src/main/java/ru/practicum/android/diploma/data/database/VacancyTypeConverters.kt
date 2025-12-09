package ru.practicum.android.diploma.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.practicum.android.diploma.domain.models.Address
import ru.practicum.android.diploma.domain.models.Contacts
import ru.practicum.android.diploma.domain.models.Employer
import ru.practicum.android.diploma.domain.models.Employment
import ru.practicum.android.diploma.domain.models.Experience
import ru.practicum.android.diploma.domain.models.FilterArea
import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.domain.models.KeySkill
import ru.practicum.android.diploma.domain.models.Salary
import ru.practicum.android.diploma.domain.models.Schedule

class VacancyTypeConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromSalary(salary: Salary?): String? {
        return salary?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toSalary(salaryJson: String?): Salary? {
        return salaryJson?.let {
            gson.fromJson(it, Salary::class.java)
        }
    }

    @TypeConverter
    fun fromAddress(address: Address?): String? {
        return address?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toAddress(addressJson: String?): Address? {
        return addressJson?.let {
            gson.fromJson(it, Address::class.java)
        }
    }

    @TypeConverter
    fun fromExperience(experience: Experience?): String? {
        return experience?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toExperience(experienceJson: String?): Experience? {
        return experienceJson?.let {
            gson.fromJson(it, Experience::class.java)
        }
    }

    @TypeConverter
    fun fromSchedule(schedule: Schedule?): String? {
        return schedule?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toSchedule(scheduleJson: String?): Schedule? {
        return scheduleJson?.let {
            gson.fromJson(it, Schedule::class.java)
        }
    }

    @TypeConverter
    fun fromEmployment(employment: Employment?): String? {
        return employment?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toEmployment(employmentJson: String?): Employment? {
        return employmentJson?.let {
            gson.fromJson(it, Employment::class.java)
        }
    }

    @TypeConverter
    fun fromContacts(contacts: Contacts?): String? {
        return contacts?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toContacts(contactsJson: String?): Contacts? {
        return contactsJson?.let {
            gson.fromJson(it, Contacts::class.java)
        }
    }

    @TypeConverter
    fun fromEmployer(employer: Employer): String {
        return gson.toJson(employer)
    }

    @TypeConverter
    fun toEmployer(employerJson: String): Employer {
        return gson.fromJson(employerJson, Employer::class.java)
    }

    @TypeConverter
    fun fromFilterArea(area: FilterArea): String {
        return gson.toJson(area)
    }

    @TypeConverter
    fun toFilterArea(areaJson: String): FilterArea {
        return gson.fromJson(areaJson, FilterArea::class.java)
    }

    @TypeConverter
    fun fromFilterIndustry(industry: FilterIndustry): String {
        return gson.toJson(industry)
    }

    @TypeConverter
    fun toFilterIndustry(industryJson: String): FilterIndustry {
        return gson.fromJson(industryJson, FilterIndustry::class.java)
    }

    @TypeConverter
    fun fromKeySkillList(list: List<KeySkill>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toKeySkillList(listJson: String): List<KeySkill> {
        val listType = object : TypeToken<List<KeySkill>>() {}.type
        return gson.fromJson(listJson, listType)
    }
}
