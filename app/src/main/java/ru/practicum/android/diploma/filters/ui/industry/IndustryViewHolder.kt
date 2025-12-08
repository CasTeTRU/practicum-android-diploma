package ru.practicum.android.diploma.filters.ui.industry

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.practicum.android.diploma.databinding.IndustryViewBinding
import ru.practicum.android.diploma.domain.models.FilterIndustry

class IndustryViewHolder(
    private val binding: IndustryViewBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: FilterIndustry, isSelected: Boolean) {
        binding.industryText.text = item.name
        binding.industryButton.isChecked = isSelected
    }

    companion object {
        fun from(parent: ViewGroup): IndustryViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = IndustryViewBinding.inflate(inflater, parent, false)
            return IndustryViewHolder(binding)
        }
    }
}
