package ru.practicum.android.diploma.filters.ui.adapter

import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemIndustryBinding
import ru.practicum.android.diploma.domain.models.FilterIndustry

class IndustryViewHolder(
    private val binding: ItemIndustryBinding,
    private val onIndustryClick: (FilterIndustry) -> Unit
) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {

    fun bind(industry: FilterIndustry, isSelected: Boolean) {
        binding.tvIndustryName.text = industry.name
        binding.ivSelected.setImageResource(
            if (isSelected) R.drawable.ic_radio_button_on else R.drawable.ic_radio_button_off
        )

        binding.root.setOnClickListener {
            onIndustryClick(industry)
        }
    }
}
