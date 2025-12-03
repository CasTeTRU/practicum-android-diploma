package ru.practicum.android.diploma.favorites.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.dto.responses.SalaryDTO
import ru.practicum.android.diploma.data.dto.responses.VacancyDetailDTO
import ru.practicum.android.diploma.databinding.VacancyViewBinding

class FavoriteVacancyAdapter(
    private val onVacancyClick: (String) -> Unit
) : ListAdapter<VacancyDetailDTO, FavoriteVacancyViewHolder>(VacancyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteVacancyViewHolder {
        val binding = VacancyViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FavoriteVacancyViewHolder(binding, onVacancyClick)
    }

    override fun onBindViewHolder(holder: FavoriteVacancyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class FavoriteVacancyViewHolder(
    private val binding: VacancyViewBinding,
    private val onVacancyClick: (String) -> Unit
) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {

    fun bind(vacancy: VacancyDetailDTO) {
        with(binding) {
            tvNameVacancy.text = "${vacancy.name}\n${vacancy.area.name}"
            tvNameCompany.text = vacancy.employer.name
            tvSalary.text = formatSalary(vacancy.salary)
            root.setOnClickListener {
                onVacancyClick(vacancy.id)
            }
        }
    }

    private fun formatSalary(salary: SalaryDTO?): String {
        if (salary == null) {
            return binding.root.context.getString(R.string.salary_not_specified)
        }
        val currency = salary.currency?.let { " $it" } ?: ""
        return when {
            salary.from != null && salary.to != null ->
                "от ${formatNumber(salary.from)} до ${formatNumber(salary.to)}$currency"
            salary.from != null ->
                "от ${formatNumber(salary.from)}$currency"
            salary.to != null ->
                "до ${formatNumber(salary.to)}$currency"
            else -> binding.root.context.getString(R.string.salary_not_specified)
        }
    }

    private fun formatNumber(number: Int?): String {
        if (number == null) return ""
        val numberString = number.toString()
        val result = StringBuilder()
        var count = 0
        for (i in numberString.length - 1 downTo 0) {
            if (count > 0 && count % DIGITS_PER_GROUP == 0) {
                result.insert(0, " ")
            }
            result.insert(0, numberString[i])
            count++
        }
        return result.toString()
    }

    companion object {
        private const val DIGITS_PER_GROUP = 3
    }
}

class VacancyDiffCallback : DiffUtil.ItemCallback<VacancyDetailDTO>() {
    override fun areItemsTheSame(oldItem: VacancyDetailDTO, newItem: VacancyDetailDTO): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: VacancyDetailDTO, newItem: VacancyDetailDTO): Boolean {
        return oldItem == newItem
    }
}

