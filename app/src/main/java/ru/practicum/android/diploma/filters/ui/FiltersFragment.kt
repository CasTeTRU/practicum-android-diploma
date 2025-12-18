package ru.practicum.android.diploma.filters.ui

import android.os.Bundle
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.filters.domain.models.FiltersParameters
import ru.practicum.android.diploma.filters.presentation.FilterScreenState
import ru.practicum.android.diploma.filters.presentation.FiltersViewModel

class FiltersFragment : Fragment() {
    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<FiltersViewModel>()
    private var salaryTextWatcher: TextWatcher? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupFragmentResultListener()
        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        setupIndustryListener()
        setupSalaryListener()
        setupCheckboxListener()
        setupActionButtons()
    }

    private fun setupObservers() {
        viewModel.filtersState.observe(viewLifecycleOwner) { state ->
            showContent(state.toParams())

            val hasFilters = state.industry != null || state.salary != null || state.onlyWithSalary
            binding.resetButton.isVisible = hasFilters
        }
        viewModel.hasChanges.observe(viewLifecycleOwner) { hasChanges ->
            binding.applyButton.isVisible = hasChanges
        }
    }

    private fun showContent(filters: FiltersParameters) = with(binding) {
        // -------- Industry --------
        val industryName = filters.industry?.name.orEmpty()

        if (etIndustry.text?.toString() != industryName) {
            etIndustry.setText(industryName)
        }

        toFilterIndustry.isVisible = filters.industry == null
        clearIndustry.isVisible = filters.industry != null

        // -------- Salary --------
        val salaryText = filters.salary?.toString().orEmpty()
        val currentText = expectedSalary.text?.toString().orEmpty()

        if (currentText != salaryText) {
            expectedSalary.setText(salaryText)
            expectedSalary.setSelection(salaryText.length)
        }

        clearIcon.isVisible = filters.salary != null

        // -------- OnlyWithSalary --------
        if (onlyWithSalaryCheckbox.isChecked != filters.onlyWithSalary) {
            onlyWithSalaryCheckbox.isChecked = filters.onlyWithSalary
        }

        clearIcon.isVisible = filters.salary != null
    }

    private fun setupIndustryListener() {
        binding.etIndustry.setOnClickListener {
            val currentState = viewModel.filtersState.value
            val bundle = Bundle().apply {
                currentState?.industry?.let { industry ->
                    putInt(KEY_CURRENT_INDUSTRY_ID, industry.id)
                    putString(KEY_CURRENT_INDUSTRY_NAME, industry.name)
                }
            }
            findNavController().navigate(R.id.action_filtersFragment_to_filterIndustryFragment, bundle)
        }
        binding.clearIndustry.setOnClickListener {
            viewModel.clearIndustry()
        }
    }

    private fun setupSalaryListener() {
        salaryTextWatcher = binding.expectedSalary.doOnTextChanged { text, _, _, _ ->
            viewModel.updateSalary(text?.toString()?.toIntOrNull())
            binding.clearIcon.isVisible = !text.isNullOrEmpty()
        }

        binding.expectedSalary.setOnEditorActionListener { _, actionId, _ ->
            actionId == EditorInfo.IME_ACTION_DONE
        }

        binding.clearIcon.setOnClickListener {
            binding.expectedSalary.text?.clear()
        }
    }

    private fun setupCheckboxListener() {
        binding.onlyWithSalaryCheckbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateOnlyWithSalary(isChecked)
        }
    }

    private fun setupActionButtons() {
        binding.applyButton.setOnClickListener {
            viewModel.applyFilters()
            findNavController().popBackStack()
        }

        binding.resetButton.setOnClickListener {
            viewModel.clearSelection()
        }
    }

    private fun FilterScreenState.toParams(): FiltersParameters {
        return FiltersParameters(
            industry = this.industry,
            salary = this.salary,
            onlyWithSalary = this.onlyWithSalary
        )
    }

    private fun setupFragmentResultListener() {
        setFragmentResultListener(REQUEST_KEY_INDUSTRY_SELECTED) { _, bundle ->
            val industryId = bundle.getInt(KEY_INDUSTRY_ID)
            val industryName = bundle.getString(KEY_INDUSTRY_NAME)
            industryName?.let { name ->
                viewModel.updateIndustry(
                    ru.practicum.android.diploma.domain.models.FilterIndustry(
                        id = industryId,
                        name = name
                    )
                )
            }
        }
    }

    companion object {
        const val REQUEST_KEY_INDUSTRY_SELECTED = "industry_selected"
        const val KEY_INDUSTRY_ID = "selected_industry_id"
        const val KEY_INDUSTRY_NAME = "selected_industry_name"
        const val KEY_CURRENT_INDUSTRY_ID = "current_industry_id"
        const val KEY_CURRENT_INDUSTRY_NAME = "current_industry_name"
    }

    override fun onDestroyView() {
        salaryTextWatcher?.let {
            binding.expectedSalary.removeTextChangedListener(it)
        }
        salaryTextWatcher = null
        _binding = null
        super.onDestroyView()
    }
}
