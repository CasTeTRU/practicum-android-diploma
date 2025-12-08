package ru.practicum.android.diploma.filters.ui.industry

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.practicum.android.diploma.domain.models.FilterIndustry

class IndustryAdapter(
    val clickListener: IndustryClickListener
) : ListAdapter<FilterIndustry, IndustryViewHolder>(IndustryDiffCallback()) {
    private var selectedId: Int? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryViewHolder =
        IndustryViewHolder.from(parent)

    override fun onBindViewHolder(holder: IndustryViewHolder, position: Int) {
        val industry = getItem(position)
        val isSelected = industry.id == selectedId

        holder.bind(industry)
        holder.check(isSelected)

        holder.itemView.setOnClickListener {
            // обновляем selection через публичный метод (чтобы логика консистентна)
            val oldSelectedId = selectedId
            if (oldSelectedId == industry.id) {
                // toggle off
                updateSelectedId(null)
                clickListener.onIndustryClick(industry) // всё ещё сообщаем о клике
            } else {
                updateSelectedId(industry.id)
                clickListener.onIndustryClick(industry)
            }
        }
    }

    fun updateSelectedId(id: Int?) {
        if (selectedId == id) return

        val oldId = selectedId
        selectedId = id

        // Найдём позиции по старому и новому id и обновим их
        val oldPos = oldId?.let { oid -> currentList.indexOfFirst { it.id == oid } } ?: -1
        val newPos = id?.let { nid -> currentList.indexOfFirst { it.id == nid } } ?: -1

        if (oldPos >= 0) notifyItemChanged(oldPos)
        if (newPos >= 0) notifyItemChanged(newPos)
    }

    class IndustryDiffCallback : DiffUtil.ItemCallback<FilterIndustry>() {
        override fun areItemsTheSame(oldItem: FilterIndustry, newItem: FilterIndustry): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: FilterIndustry, newItem: FilterIndustry): Boolean =
            oldItem == newItem
    }

    fun interface IndustryClickListener {
        fun onIndustryClick(industry: FilterIndustry)
    }
}
