package ru.practicum.android.diploma.filters.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.practicum.android.diploma.databinding.ItemIndustryBinding
import ru.practicum.android.diploma.domain.models.FilterIndustry

class IndustryAdapter(
    private val onIndustryClick: (FilterIndustry) -> Unit
) : ListAdapter<FilterIndustry, IndustryViewHolder>(IndustryDiffCallback()) {

    private var selectedIndustryId: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IndustryViewHolder {
        val binding = ItemIndustryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return IndustryViewHolder(binding, onIndustryClick)
    }

    override fun onBindViewHolder(holder: IndustryViewHolder, position: Int) {
        val industry = getItem(position)

        val isSelected = industry.id == selectedIndustryId
        holder.bind(industry, isSelected)
    }

    fun setSelectedIndustry(industryId: Int?) {
        val previousSelected = selectedIndustryId
        if (previousSelected == industryId) return
        selectedIndustryId = industryId
        val indicesToUpdate = mutableSetOf<Int>()
        previousSelected?.let { id ->
            currentList.indexOfFirst { it.id == id }.takeIf { it != -1 }?.let(indicesToUpdate::add)
        }
        industryId?.let { id ->
            currentList.indexOfFirst { it.id == id }.takeIf { it != -1 }?.let(indicesToUpdate::add)
        }
        indicesToUpdate.forEach { notifyItemChanged(it) }
    }

    fun getSelectedIndustryId(): Int? = selectedIndustryId

    class IndustryDiffCallback : DiffUtil.ItemCallback<FilterIndustry>() {
        override fun areItemsTheSame(oldItem: FilterIndustry, newItem: FilterIndustry): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FilterIndustry, newItem: FilterIndustry): Boolean {
            return oldItem == newItem
        }
    }
}
