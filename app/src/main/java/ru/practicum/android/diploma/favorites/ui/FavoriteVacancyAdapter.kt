package ru.practicum.android.diploma.favorites.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.dto.responses.SalaryDTO
import ru.practicum.android.diploma.data.dto.responses.VacancyDetailDTO
import ru.practicum.android.diploma.databinding.VacancyViewBinding
import java.text.NumberFormat
import java.util.Locale

class FavoriteVacancyAdapter(
    private val onItemClick: (String) -> Unit
) : ListAdapter<VacancyDetailDTO, FavoriteVacancyAdapter.FavoriteVacancyViewHolder>(
    FavoriteVacancyDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteVacancyViewHolder {
        val binding = VacancyViewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FavoriteVacancyViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: FavoriteVacancyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FavoriteVacancyViewHolder(
        private val binding: VacancyViewBinding,
        private val onItemClick: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(vacancy: VacancyDetailDTO) {
            binding.root.setOnClickListener {
                onItemClick(vacancy.id)
            }

            binding.tvNameVacancy.text =
                formatVacancyNameWithArea(vacancy.name, vacancy.area?.name)

            binding.tvNameCompany.text = vacancy.employer?.name ?: "Компания не указана"

            binding.tvSalary.text = formatSalary(vacancy.salary)

            loadCompanyLogo(vacancy.employer?.logo)
        }

        private fun formatSalary(salary: SalaryDTO?): String {
            if (salary == null) return "Зарплата не указана"

            val from = salary.from
            val to = salary.to
            val currency = salary.currency.orEmpty()

            val formattedFrom = from?.let(::formatNumber)
            val formattedTo = to?.let(::formatNumber)

            return when {
                formattedFrom != null && formattedTo != null ->
                    "от $formattedFrom до $formattedTo $currency".trim()

                formattedFrom != null ->
                    "от $formattedFrom $currency".trim()

                formattedTo != null ->
                    "до $formattedTo $currency".trim()

                else -> "Зарплата не указана"
            }
        }

        private fun formatNumber(number: Int): String =
            NumberFormat.getNumberInstance(Locale("ru")).format(number)

        private fun formatVacancyNameWithArea(vacancyName: String, areaName: String?): String {
            return if (!areaName.isNullOrBlank()) {
                "$vacancyName, $areaName"
            } else {
                vacancyName
            }
        }

        private fun loadCompanyLogo(logoUrl: String?) {
            val context = binding.root.context

            val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_company_logo_placeholder)
                .error(R.drawable.ic_company_logo_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)

            Glide.with(context)
                .load(logoUrl ?: R.drawable.ic_company_logo_placeholder)
                .apply(requestOptions)
                .into(binding.imgCompany)
        }
    }

    class FavoriteVacancyDiffCallback : DiffUtil.ItemCallback<VacancyDetailDTO>() {
        override fun areItemsTheSame(oldItem: VacancyDetailDTO, newItem: VacancyDetailDTO): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: VacancyDetailDTO, newItem: VacancyDetailDTO): Boolean {
            return oldItem == newItem
        }
    }
}
