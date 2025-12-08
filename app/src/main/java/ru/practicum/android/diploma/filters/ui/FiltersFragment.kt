package ru.practicum.android.diploma.filters.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterBinding
import ru.practicum.android.diploma.filters.presentation.FiltersViewModel

class FiltersFragment : Fragment() {
    private var _binding: FragmentFilterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FiltersViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupIndustryField()
        observeViewModel()
        setupFragmentResultListener()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupIndustryField() {
        binding.etIndustry.setOnClickListener {
            findNavController().navigate(R.id.action_filtersFragment_to_filterIndustryFragment)
        }
    }

    private fun observeViewModel() {
        viewModel.filtersState.observe(viewLifecycleOwner) { state ->
            binding.etIndustry.setText(state.industryName ?: "")
        }
    }

    private fun setupFragmentResultListener() {
        setFragmentResultListener(REQUEST_KEY_INDUSTRY_SELECTED) { _, bundle ->
            val industryId = bundle.getInt(KEY_INDUSTRY_ID)
            val industryName = bundle.getString(KEY_INDUSTRY_NAME)
            industryName?.let { name ->
                viewModel.saveIndustry(
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
