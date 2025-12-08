package ru.practicum.android.diploma.filters.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.filters.presentation.FiltersViewModel
import ru.practicum.android.diploma.util.showIf

class FiltersFragment : Fragment() {
    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!
    private var textWatcher: TextWatcher? = null
    private val viewModel by viewModel<FiltersViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()

        viewModel.filtersState.observe(viewLifecycleOwner) { state ->
            val hasFilters = state.industry != null || state.salary != null || state.onlyWithSalary
            binding.applyButton.showIf(hasFilters)
            binding.resetButton.showIf(hasFilters)

            binding.etIndustry.setText(state?.industry?.name ?: "")

            updateSalaryField(state.salary)

            binding.onlyWithSalaryCheckbox.isChecked = state.onlyWithSalary
        }
    }

    private fun updateSalaryField(salary: Int?) {
        textWatcher?.let { binding.expectedSalary.removeTextChangedListener(it) }

        val text = salary?.toString() ?: ""
        binding.expectedSalary.setText(text)
        // Устанавливаем курсор в конец
        binding.expectedSalary.setSelection(text.length)

        textWatcher?.let { binding.expectedSalary.addTextChangedListener(it) }
    }

    private fun initUI() {
        setupCallback()
        setupExpectedSalary()
        setupOnlyWithSalaryCheckbox()
        setupToolbar()
    }

    private fun setupExpectedSalary() = with(binding) {
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val salary = s.toString().toIntOrNull()
                viewModel.updateSalary(salary)
            }
            override fun afterTextChanged(s: Editable?) = Unit
        }
        textWatcher?.let { expectedSalary.addTextChangedListener(it) }
    }

    private fun setupOnlyWithSalaryCheckbox() = with(binding) {
        onlyWithSalaryCheckbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.updateOnlyWithSalary(isChecked)
        }
    }

    private fun setupCallback() {
        binding.industryBtn.setOnClickListener {
            findNavController().navigate(R.id.action_filtersFragment_to_industryFragment, Bundle.EMPTY)
        }
        binding.applyButton.setOnClickListener {
            viewModel.applyFilters()

            findNavController().popBackStack()
        }

        binding.resetButton.setOnClickListener {
            viewModel.clearSelection()
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            viewModel.restorePreviousState()
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadSavedFilters()
    }
}
