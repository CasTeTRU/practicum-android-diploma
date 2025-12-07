package ru.practicum.android.diploma.favorites.ui.adapter

import android.content.res.Configuration
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.VacancyViewBinding
import ru.practicum.android.diploma.util.SalaryFormatter
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailed

class FavoriteVacancyViewHolder(
    private val binding: VacancyViewBinding,
    private val onVacancyClick: (String) -> Unit
) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {

    fun bind(vacancy: VacancyDetailed) = with(binding) {
        val context = itemView.context
        val cornerSize = itemView.resources.getDimensionPixelSize(R.dimen.corner_radius)
        val isNightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK ==
            Configuration.UI_MODE_NIGHT_YES
        val placeholder = if (isNightMode) {
            R.drawable.ic_company_logo_placeholder_night
        } else {
            R.drawable.ic_company_logo_placeholder
        }

        val imageUrl = vacancy.employer?.logo?.takeIf { it.isNotBlank() }
        Glide.with(itemView)
            .load(imageUrl)
            .centerCrop()
            .placeholder(placeholder)
            .fallback(placeholder)
            .error(placeholder)
            .transform(RoundedCorners(cornerSize))
            .into(imgCompany)

        val employerName = vacancy.employer?.name.orEmpty()
        val location = vacancy.address?.city.takeIf { !it.isNullOrBlank() }
            ?: vacancy.area?.name

        tvNameVacancy.text = context.getString(R.string.two_params, vacancy.name, location)
        tvNameCompany.text = employerName
        tvSalary.text = SalaryFormatter.format(context, vacancy.salary)

        root.setOnClickListener {
            onVacancyClick(vacancy.id)
        }
    }
}
