package ru.practicum.android.diploma.favorites.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import ru.practicum.android.diploma.databinding.VacancyViewBinding
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailed

class FavoriteVacancyAdapter(
    private val onVacancyClick: (String) -> Unit
) : androidx.recyclerview.widget.ListAdapter<VacancyDetailed, FavoriteVacancyViewHolder>(VacancyDiffCallback()) {

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

class VacancyDiffCallback : DiffUtil.ItemCallback<VacancyDetailed>() {
    override fun areItemsTheSame(oldItem: VacancyDetailed, newItem: VacancyDetailed): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: VacancyDetailed, newItem: VacancyDetailed): Boolean {
        return oldItem == newItem
    }
}
