package ru.practicum.android.diploma.search.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.search.domain.models.Vacancy

class SearchAdapter(val clickListener: SearchClickListener) :
    RecyclerView.Adapter<VacancyViewHolder>() {
    var vacanciesList = ArrayList<Vacancy>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VacancyViewHolder =
        VacancyViewHolder.from(parent)

    override fun onBindViewHolder(holder: VacancyViewHolder, position: Int) {
        holder.bind(vacanciesList[position])
        holder.itemView.setOnClickListener {
            clickListener.onVacancyClick(vacanciesList[position])
        }
    }

    override fun getItemCount(): Int = vacanciesList.size

    fun interface SearchClickListener {
        fun onVacancyClick(vacancy: Vacancy)
    }
}
