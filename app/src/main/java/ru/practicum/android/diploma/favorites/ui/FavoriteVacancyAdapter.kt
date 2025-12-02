package ru.practicum.android.diploma.favorites.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
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
        return when {
            salary == null -> ""
            salary.from != null && salary.to != null -> 
                "от ${formatNumber(salary.from)} до ${formatNumber(salary.to)} ${salary.currency ?: ""}"
            salary.from != null -> 
                "от ${formatNumber(salary.from)} ${salary.currency ?: ""}"
            salary.to != null -> 
                "до ${formatNumber(salary.to)} ${salary.currency ?: ""}"
            else -> ""
        }
    }

    private fun formatNumber(number: Int?): String {
        return number?.toString()?.reversed()?.chunked(3)?.joinToString(" ")?.reversed() ?: ""
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

