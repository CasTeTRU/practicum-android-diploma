package ru.practicum.android.diploma.filters.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterIndustryBinding
import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.filters.presentation.FilterIndustryScreenState
import ru.practicum.android.diploma.filters.presentation.FilterIndustryViewModel
import ru.practicum.android.diploma.filters.ui.adapter.IndustryAdapter
import ru.practicum.android.diploma.util.UiError

class FilterIndustryFragment : Fragment() {
    private var _binding: FragmentFilterIndustryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FilterIndustryViewModel by viewModel()
    private lateinit var adapter: IndustryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilterIndustryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupToolbar()
        setupSearchField()
        setupApplyButton()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = IndustryAdapter { industry ->
            viewModel.onIndustrySelected(industry)
            adapter.setSelectedIndustry(industry.id)
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupSearchField() {
        binding.searchIndustry.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s?.toString() ?: ""
                viewModel.filterIndustries(query)
                updateSearchIcons(query.isNotEmpty())
            }
        })
    }

    private fun updateSearchIcons(hasText: Boolean) {
        binding.searchFieldIcon.isVisible = !hasText
        binding.clearIcon.isVisible = hasText
    }

    private fun setupApplyButton() {
        binding.clearIcon.setOnClickListener {
            binding.searchIndustry.setText("")
            viewModel.filterIndustries("")
            updateSearchIcons(false)
        }

        binding.applyButton.setOnClickListener {
            viewModel.getSelectedIndustry()?.let { industry ->
                val result = Bundle().apply {
                    putInt(FiltersFragment.KEY_INDUSTRY_ID, industry.id)
                    putString(FiltersFragment.KEY_INDUSTRY_NAME, industry.name)
                }
                parentFragmentManager.setFragmentResult(
                    FiltersFragment.REQUEST_KEY_INDUSTRY_SELECTED,
                    result
                )
                findNavController().popBackStack()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.screenState.observe(viewLifecycleOwner) { state ->
            render(state)
        }

        viewModel.selectedIndustry.observe(viewLifecycleOwner) { industry ->
            binding.applyButton.isVisible = industry != null
            // Обновляем выбранный элемент в адаптере при изменении
            industry?.let {
                adapter.setSelectedIndustry(it.id)
            } ?: run {
                adapter.setSelectedIndustry(null)
            }
        }
    }

    private fun render(state: FilterIndustryScreenState) {
        when (state) {
            is FilterIndustryScreenState.Loading -> {
                showLoading()
            }
            is FilterIndustryScreenState.Content -> {
                showContent(state.industries)
            }
            is FilterIndustryScreenState.Error -> {
                showError(state.error)
            }
        }
    }

    private fun showLoading() {
        binding.recyclerView.visibility = View.GONE
        binding.errorPlaceholder.visibility = View.GONE
    }

    private fun showContent(industries: List<FilterIndustry>) {
        binding.recyclerView.visibility = View.VISIBLE
        binding.errorPlaceholder.visibility = View.GONE
        val selectedId = adapter.getSelectedIndustryId() ?: viewModel.getSelectedIndustry()?.id
        adapter.submitList(industries) {
            // Восстанавливаем выбранный элемент после обновления списка
            if (selectedId != null) {
                adapter.setSelectedIndustry(selectedId)
            }
        }
    }

    private fun showError(error: UiError) {
        binding.recyclerView.visibility = View.GONE
        binding.errorPlaceholder.visibility = View.VISIBLE
        binding.errorPlaceholder.text = when (error) {
            UiError.NoInternet -> getString(R.string.error_no_internet)
            UiError.ServerError -> getString(R.string.error_server)
            UiError.NothingFound -> getString(R.string.error_nothing_found)
            is UiError.Unknown -> getString(R.string.error_server)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
