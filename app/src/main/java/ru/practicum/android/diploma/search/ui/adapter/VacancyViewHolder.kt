package ru.practicum.android.diploma.search.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancyViewBinding
import ru.practicum.android.diploma.search.domain.models.Vacancy

class VacancyViewHolder(private val binding : VacancyViewBinding)
: RecyclerView.ViewHolder(binding.root) {

    fun bind(vacancy: Vacancy) = with(binding) {
        val imageUrl = vacancy.url?.takeIf { it.isNotBlank() }
        val cornerSize = itemView.resources.getDimensionPixelSize(R.dimen.corner_radius)

        Glide.with(itemView)
            .load(imageUrl)
            .centerCrop()
            .placeholder(R.drawable.ic_company_logo_placeholder)
            .transform(RoundedCorners(cornerSize))
            .into(imgCompany)

        tvSalary.text = vacancy.salary?.currency
        tvNameVacancy.text = vacancy.employerName
    }

    companion object {
        fun from(parent: ViewGroup): VacancyViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = VacancyViewBinding.inflate(inflater, parent, false)
            return VacancyViewHolder(binding)
        }
    }
}
