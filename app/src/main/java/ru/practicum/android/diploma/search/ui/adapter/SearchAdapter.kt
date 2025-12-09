package ru.practicum.android.diploma.search.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.practicum.android.diploma.search.domain.models.Vacancy

class SearchAdapter(
    val clickListener: SearchClickListener
) : ListAdapter<Vacancy, VacancyViewHolder>(VacancyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder =
        VacancyViewHolder.from(parent)

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        val vacancy = getItem(position)

        holder.bind(vacancy)
        holder.itemView.setOnClickListener {
            clickListener.onVacancyClick(vacancy)
        }
    }

    class VacancyDiffCallback : DiffUtil.ItemCallback<Vacancy>() {
        override fun areItemsTheSame(oldItem: Vacancy, newItem: Vacancy): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Vacancy, newItem: Vacancy): Boolean =
            oldItem == newItem
    }

    fun interface SearchClickListener {
        fun onVacancyClick(vacancy: Vacancy)
    }
}
