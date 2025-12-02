package ru.practicum.android.diploma.favorites.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentFavoritesBinding
import ru.practicum.android.diploma.favorites.presentation.FavoritesScreenState
import ru.practicum.android.diploma.favorites.presentation.FavoritesViewModel

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<FavoritesViewModel>()
    
    private val adapter = FavoriteVacancyAdapter { vacancyId ->
//тут нужна навигация на экран деталей вакансии
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewFavorites.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFavorites.adapter = adapter
    }

    private fun observeViewModel() {
        viewModel.favoritesState.observe(viewLifecycleOwner) { state ->
            render(state)
        }
    }

    private fun render(state: FavoritesScreenState) {
        when (state) {
            is FavoritesScreenState.Loading -> {
                showLoading()
            }
            is FavoritesScreenState.Empty -> {
                showEmptyState()
            }
            is FavoritesScreenState.Content -> {
                showFavoritesList(state.favorites)
            }
            is FavoritesScreenState.Error -> {
                showErrorState()
            }
        }
    }

    private fun showLoading() {
        binding.apply {
            emptyStateContainer.visibility = View.GONE
            errorStateContainer.visibility = View.GONE
            recyclerViewFavorites.visibility = View.GONE
        }
    }

    private fun showEmptyState() {
        binding.apply {
            emptyStateContainer.visibility = View.VISIBLE
            errorStateContainer.visibility = View.GONE
            recyclerViewFavorites.visibility = View.GONE
        }
    }

    private fun showFavoritesList(favorites: List<ru.practicum.android.diploma.data.dto.responses.VacancyDetailDTO>) {
        binding.apply {
            emptyStateContainer.visibility = View.GONE
            errorStateContainer.visibility = View.GONE
            recyclerViewFavorites.visibility = View.VISIBLE
            adapter.submitList(favorites)
        }
    }

    private fun showErrorState() {
        binding.apply {
            emptyStateContainer.visibility = View.GONE
            errorStateContainer.visibility = View.VISIBLE
            recyclerViewFavorites.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
