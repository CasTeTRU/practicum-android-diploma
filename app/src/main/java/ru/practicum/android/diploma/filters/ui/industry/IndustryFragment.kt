package ru.practicum.android.diploma.filters.ui.industry

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentIndustryBinding
import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.filters.presentation.IndustryScreenState
import ru.practicum.android.diploma.filters.presentation.IndustryViewModel
import ru.practicum.android.diploma.util.UiError
import ru.practicum.android.diploma.util.debounce
import ru.practicum.android.diploma.util.hide
import ru.practicum.android.diploma.util.show
import ru.practicum.android.diploma.util.showIf

class IndustryFragment : Fragment() {
    private var _binding: FragmentIndustryBinding? = null
    private val binding get() = _binding!!
    private var textWatcher: TextWatcher? = null
    private val viewModel by viewModel<IndustryViewModel>()
    private var onIndustryClickDebounce: ((industryId: FilterIndustry) -> Unit)? = null
    private var industryAdapter = IndustryAdapter { industry ->
        onIndustryClickDebounce?.invoke(industry)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIndustryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()

        viewModel.industryStatusLiveData.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun initUI() {
        setupRecyclerView()
        setupSearchField()
        setupToolbar()
        setupIndustryClickDebounce()
        setupClearIcon()
        setupApplyButton()
    }

    private fun setupApplyButton() = with(binding) {
        applyButton.setOnClickListener {
            viewModel.applySelection()
            findNavController().popBackStack()
        }
    }

    private fun setupClearIcon() = with(binding) {
        clearIcon.setOnClickListener {
            searchIndustry.setText("")
            viewModel.onSearchInput("")
        }
    }

    private fun setupSearchField() = with(binding) {
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) =
                Unit

            override fun afterTextChanged(s: Editable?) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val currentText = s?.toString() ?: ""
                viewModel.onSearchInput(currentText)
                binding.clearIcon.showIf(currentText.isNotEmpty())
                binding.searchIcon.showIf(currentText.isEmpty())
            }
        }

        textWatcher?.let { binding.searchIndustry.addTextChangedListener(it) }
    }

    private fun setupRecyclerView() = with(binding) {
        if (recyclerView.layoutManager == null) {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
        recyclerView.adapter = industryAdapter

        val initialSelected = viewModel.industryStatusLiveData.value?.selected
        industryAdapter.updateSelectedId(initialSelected?.id)
    }

    private fun setupIndustryClickDebounce() {
        onIndustryClickDebounce = debounce(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { industry ->
            viewModel.selectIndustry(industry)
        }
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun render(state: IndustryScreenState): Unit = with(binding) {
        hideAllView()

        applyButton.showIf(state.selected != null)
        progressBar.showIf(state.isLoading)

        if (state.isLoading) return

        if (state.industryList.isNotEmpty()) {
            renderContent(state.industryList)
        }

        state.error?.let { renderError(it) }
    }

    private fun hideAllView() = with(binding) {
        progressBar.hide()
        recyclerView.hide()
        errorPlaceholder.hide()
    }

    private fun renderContent(favorites: List<FilterIndustry>) = with(binding) {
        progressBar.hide()
        recyclerView.show()
        industryAdapter.submitList(favorites)
    }

    private fun renderError(error: UiError) {
        binding.apply {
            errorPlaceholder.visibility = View.VISIBLE
            errorPlaceholder.text = when (error) {
                UiError.NoInternet -> getString(R.string.error_no_internet)
                UiError.ServerError -> getString(R.string.error_server)
                UiError.NothingFound -> getString(R.string.no_list)
                is UiError.Unknown -> getString(R.string.error_server)
            }
            val drawable = when (error) {
                UiError.NoInternet -> R.drawable.img_error_connection
                UiError.ServerError -> R.drawable.img_error_connection
                UiError.NothingFound -> R.drawable.img_error_nothing_found
                is UiError.Unknown -> R.drawable.img_error_connection
            }

            errorPlaceholder.setCompoundDrawablesWithIntrinsicBounds(
                0,
                drawable,
                0,
                0
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
