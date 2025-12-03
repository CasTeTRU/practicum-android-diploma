package ru.practicum.android.diploma.search.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancyViewBinding
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.search.ui.utils.SalaryFormatter

class VacancyViewHolder(private val binding: VacancyViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(vacancy: Vacancy) = with(binding) {
        val context = itemView.context
        val cornerSize = itemView.resources.getDimensionPixelSize(R.dimen.corner_radius)

        val imageUrl = vacancy.employerLogo?.takeIf { it.isNotBlank() }
        Glide.with(itemView)
            .load(imageUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_company_logo_placeholder)
            .fallback(R.drawable.ic_company_logo_placeholder)
            .error(R.drawable.ic_company_logo_placeholder)
            .transform(RoundedCorners(cornerSize))
            .into(imgCompany)

        val employerName = vacancy.employerName.orEmpty()
        val location = vacancy.address?.city.takeIf { !it.isNullOrBlank() }
            ?: vacancy.areaName
            ?: ""

        tvNameVacancy.text = context.getString(R.string.vacancy_with_location, vacancy.name, location)
        tvNameCompany.text = employerName
        tvSalary.text = SalaryFormatter.format(context, vacancy.salary)
    }

    companion object {
        fun from(parent: ViewGroup): VacancyViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = VacancyViewBinding.inflate(inflater, parent, false)
            return VacancyViewHolder(binding)
        }
    }
}
