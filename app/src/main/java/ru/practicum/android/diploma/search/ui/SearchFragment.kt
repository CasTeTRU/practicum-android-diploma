package ru.practicum.android.diploma.search.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.search.domain.models.Vacancy
import ru.practicum.android.diploma.search.presentation.SearchScreenState
import ru.practicum.android.diploma.search.presentation.SearchViewModel
import ru.practicum.android.diploma.search.ui.adapter.SearchAdapter
import ru.practicum.android.diploma.util.UiError
import ru.practicum.android.diploma.util.debounce
import ru.practicum.android.diploma.vacancy.ui.VacancyFragment

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private var textWatcher: TextWatcher? = null
    private val viewModel by viewModel<SearchViewModel>()
    private var onVacancyClickDebounce: ((vacancyId: String) -> Unit)? = null
    private var searchAdapter = SearchAdapter { vacancy ->
        onVacancyClickDebounce?.invoke(vacancy.id)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()

        viewModel.searchStatusLiveData.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun initUI() {
        setupRecyclerView()
        setupSearchField()
        setupToolbar()
        setupVacancyClickDebounce()
        setupClearIcon()
    }

    private fun setupRecyclerView() = with(binding) {
        if (recyclerView.layoutManager == null) {
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
        // Recycler View
        recyclerView.adapter = searchAdapter

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy <= 0) return // нас интересует только скролл вниз

                val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return
                val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                val totalItems = searchAdapter.itemCount
                // если достигли конца — просим ViewModel подгрузить
                if (lastVisiblePosition >= totalItems - 1) {
                    viewModel.fetchNextPage()
                }
            }
        })
    }

    private fun setupSearchField() {
        // Убираем двойные вызовы: используем только onTextChanged для запуска дебаунса
        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

            override fun afterTextChanged(s: Editable?) = Unit

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val currentText = s?.toString() ?: ""
                viewModel.onQueryChanged(currentText)
                updateClearButtonVisibility(currentText)
            }
        }

        textWatcher?.let { binding.searchField.addTextChangedListener(it) }
    }

    private fun setupToolbar() {
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.actionFilter -> {
                    findNavController().navigate(
                        R.id.action_searchFragment_to_filtersFragment,
                        Bundle.EMPTY
                    )
                    true
                }
                else -> false
            }
        }
    }

    private fun setupVacancyClickDebounce() {
        onVacancyClickDebounce = debounce<String>(
            CLICK_DEBOUNCE_DELAY,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { vacancyId ->
            val bundle = bundleOf(VacancyFragment.ARG_VACANCY to vacancyId)
            findNavController().navigate(R.id.action_searchFragment_to_vacancyFragment, bundle)
        }
    }

    private fun setupClearIcon() = with(binding) {
        clearIcon.setOnClickListener {
            searchField.setText("")
            updateClearButtonVisibility("")
            viewModel.onQueryChanged("")
        }
    }

    private fun hideAllView() = with(binding) {
        progressBar.visibility = View.GONE
        recyclerView.visibility = View.GONE
        searchInfo.visibility = View.GONE
        searchScreenCover.visibility = View.GONE
        errorPlaceholder.visibility = View.GONE
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun render(state: SearchScreenState): Unit = with(binding) {
        hideAllView()

        // базовые индикаторы
        progressBar.visibility = if (state.isLoading || state.isFetching) View.VISIBLE else View.GONE

        if (state.isLoading) return

        // Контент
        if (state.vacancies.isNotEmpty()) {
            renderContent(state.vacancies)
        } else if (state.error == null) {
            searchScreenCover.visibility = View.VISIBLE
        }

        // Ошибки
        state.error?.let { renderError(it) }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun renderContent(vacancies: List<Vacancy>) {
        binding.apply {
            recyclerView.visibility = View.VISIBLE

            searchAdapter.submitList(vacancies)
            searchAdapter.notifyDataSetChanged()
        }
    }

    private fun renderError(error: UiError) {
        binding.apply {
            errorPlaceholder.visibility = View.VISIBLE
            errorPlaceholder.text = when (error) {
                UiError.NoInternet -> getString(R.string.error_no_internet)
                UiError.ServerError -> getString(R.string.error_server)
                UiError.NothingFound -> getString(R.string.error_nothing_found)
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

            if (error == UiError.NothingFound) {
                searchInfo.visibility = View.VISIBLE
                searchInfo.text = getString(R.string.empty_vacancy)
            }
        }
    }

    private fun updateClearButtonVisibility(text: String) {
        val shouldShowClearButton = text.isNotEmpty()
        if (shouldShowClearButton) {
            binding.searchIcon.visibility = View.GONE
            binding.clearIcon.visibility = View.VISIBLE
        } else {
            binding.searchIcon.visibility = View.VISIBLE
            binding.clearIcon.visibility = View.GONE
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        textWatcher.let { binding.searchField.removeTextChangedListener(it) }
        _binding = null
        onVacancyClickDebounce = null
    }

    companion object {
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
