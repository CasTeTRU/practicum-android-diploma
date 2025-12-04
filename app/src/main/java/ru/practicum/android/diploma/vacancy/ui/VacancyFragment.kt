package ru.practicum.android.diploma.vacancy.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.search.presentation.UiError
import ru.practicum.android.diploma.util.SalaryFormatter
import ru.practicum.android.diploma.vacancy.domain.models.VacancyDetailed
import ru.practicum.android.diploma.vacancy.presentation.VacancyScreenState
import ru.practicum.android.diploma.vacancy.presentation.VacancyViewModel

class VacancyFragment : Fragment() {
    private var _binding: FragmentVacancyBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<VacancyViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentVacancyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getVacancyIdFromArgs()

        viewModel.vacancyStatusLiveData.observe(viewLifecycleOwner) {
            render(it)
        }

        setupToolbar()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: VacancyScreenState) {

        when (state) {
            is VacancyScreenState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }
            is VacancyScreenState.ShowContent -> {
                renderContent(state.vacancy)
            }
            is VacancyScreenState.Error -> {
                renderError(state.error)
            }
        }
    }

    private fun renderContent(vacancy: VacancyDetailed) = with(binding) {
        tvNameVacancy.text = vacancy.name
        tvSalaryVacancy.text = SalaryFormatter.format(requireContext(), vacancy.salary)

        experience.text = vacancy.experience?.name
        tvNameCompany.text = vacancy.employer?.name
        tvCompanyCity.text = vacancy.address?.city

        viewCompanyLogo(vacancy)
    }

    private fun viewCompanyLogo(vacancy: VacancyDetailed) {
        val cornerSize = resources.getDimensionPixelSize(R.dimen.corner_radius)
        Glide.with(this)
            .load(vacancy.employer?.logo)
            .placeholder(R.drawable.ic_company_logo_placeholder)
            .transform(RoundedCorners(cornerSize))
            .into(binding.icCompany)
    }

    private fun renderError(error: UiError) {
    }

    private fun getVacancyIdFromArgs() {
        val vacancyId = requireArguments().getString(ARG_VACANCY)

        if (!vacancyId.isNullOrEmpty()) {
            viewModel.searchVacancyById(vacancyId)
        }
    }

    companion object {
        const val ARG_VACANCY = "vacancy_id"
    }
}
