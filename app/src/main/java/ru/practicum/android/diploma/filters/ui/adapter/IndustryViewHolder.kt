package ru.practicum.android.diploma.filters.ui.adapter

import android.view.View
import ru.practicum.android.diploma.databinding.ItemIndustryBinding
import ru.practicum.android.diploma.domain.models.FilterIndustry

class IndustryViewHolder(
    private val binding: ItemIndustryBinding,
    private val onIndustryClick: (FilterIndustry) -> Unit
) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {

    fun bind(industry: FilterIndustry, isSelected: Boolean) {
        binding.tvIndustryName.text = industry.name
        binding.ivSelected.visibility = if (isSelected) View.VISIBLE else View.GONE

        binding.root.setOnClickListener {
            onIndustryClick(industry)
        }
    }
}
