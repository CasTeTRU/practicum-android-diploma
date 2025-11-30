package ru.practicum.android.diploma.vacancy.domain.models

data class Vacancy(
    val id: String,
    val name : String,
    val description: String,
    val salary: Salary?,
    val address: Address,
    val experience: String,
    val schedule: String,
    val employment: String,
    val contacts: Contacts?,
    val employer : Employer,
    val skills:List<String>,
    val linkUrl: String
)


data class Salary(
    val from: Int?,
    val to: Int?,
    val currency: String?
)

data class Address(
    val city: String,
    val raw: String
)


data class Phone(
    val comment: String,
    val formatted: String
)

data class Contacts(
    val name: String,
    val email: String,
    val phones: List<Phone>
)

data class Employer(
    val name: String,
    val logo: String?
)




