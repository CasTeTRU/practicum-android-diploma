package ru.practicum.android.diploma.favorites.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import ru.practicum.android.diploma.databinding.FragmentFavoritesBinding
import ru.practicum.android.diploma.favorites.domain.api.FavoriteInteractor

class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val favoriteInteractor: FavoriteInteractor by inject()

    private val adapter = FavoriteVacancyAdapter { vacancyId ->
                // findNavController().navigate(...)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadFavorites()
    }

    private fun loadFavorites() {
        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val favorites = favoriteInteractor.getFavorites()
                showFavoritesList(favorites)
            } catch (e: Exception) {
                showErrorState()
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewFavorites.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFavorites.adapter = adapter
    }

    private fun showEmptyState() {
        binding.emptyStateContainer.visibility = View.VISIBLE
        binding.errorStateContainer.visibility = View.GONE
        binding.recyclerViewFavorites.visibility = View.GONE
    }

    private fun showErrorState() {
        binding.emptyStateContainer.visibility = View.GONE
        binding.errorStateContainer.visibility = View.VISIBLE
        binding.recyclerViewFavorites.visibility = View.GONE
    }

    private fun showFavoritesList(vacancies: List<ru.practicum.android.diploma.data.dto.responses.VacancyDetailDTO>) {
        if (vacancies.isEmpty()) {
            showEmptyState()
        } else {
            binding.emptyStateContainer.visibility = View.GONE
            binding.errorStateContainer.visibility = View.GONE
            binding.recyclerViewFavorites.visibility = View.VISIBLE
            adapter.submitList(vacancies)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
