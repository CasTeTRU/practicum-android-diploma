package ru.practicum.android.diploma.vacancy.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.domain.models.Address
import ru.practicum.android.diploma.domain.models.Contacts
import ru.practicum.android.diploma.domain.models.Employment
import ru.practicum.android.diploma.domain.models.FilterArea
import ru.practicum.android.diploma.domain.models.KeySkill
import ru.practicum.android.diploma.domain.models.Schedule
import ru.practicum.android.diploma.util.SalaryFormatter
import ru.practicum.android.diploma.util.UiEvent
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
        observeViewModel()
        observeEvents()
        setupToolbar()
    }

    private fun observeViewModel() {
        viewModel.vacancyStatusLiveData.observe(viewLifecycleOwner) {
            render(it)
        }
    }

    private fun observeEvents() {
        viewModel.events.observe(viewLifecycleOwner) { event ->
            when (event) {
                is UiEvent.ShowMessage ->
                    Snackbar.make(requireView(), event.message, Snackbar.LENGTH_SHORT).show()

                is UiEvent.ShowError ->
                    Snackbar.make(requireView(), event.error.toString(), Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun setupToolbar() = with(binding) {
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_favorite -> {
                    viewModel.toggleFavorite()
                    true
                }

                R.id.action_share -> {
                    viewModel.shareLink()
                    true
                }

                else -> false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: VacancyScreenState) {
        hideAllView()

        when (state) {
            is VacancyScreenState.Loading -> {
                binding.progressBar.visibility = View.VISIBLE
            }

            is VacancyScreenState.ShowContent -> {
                renderContent(state.vacancy, state.isFavorite)
            }

            is VacancyScreenState.Error -> {
                renderError()
            }
        }
    }

    private fun hideAllView() = with(binding) {
        listOf(
            scrollView, errorContainer, progressBar, tvNameVacancy,
            tvSalaryVacancy, companyCard, experience, tvTitleNeededExperience,
            tvEmploymentType, tvTitleDescription, tvDescription, tvTitleContacts
        ).forEach { it.hide() }
    }

    private fun renderContent(vacancy: VacancyDetailed, isFavorite: Boolean) = with(binding) {
        listOf(
            tvNameVacancy, scrollView, tvSalaryVacancy, companyCard,
            experience, tvTitleNeededExperience, tvEmploymentType,
            tvTitleDescription, tvDescription, tvTitleContacts
        ).forEach { it.show() }

        tvNameVacancy.text = vacancy.name
        tvSalaryVacancy.text = SalaryFormatter.format(requireContext(), vacancy.salary)
        tvDescription.text = vacancy.description
        tvTitleNeededExperience.text = vacancy.experience?.name
        tvNameCompany.text = vacancy.employer?.name

        viewCompanyLogo(vacancy)
        viewEmploymentAndSchedule(vacancy.employment, vacancy.schedule)
        viewCityOrRegion(vacancy?.area, vacancy?.address)
        updateFavoriteIcon(isFavorite)
        viewKeySkills(vacancy.keySkills)
        viewContacts(vacancy.contacts)
    }

    private fun viewEmploymentAndSchedule(employment: Employment?, schedule: Schedule?) {
        val employment = employment?.name
        val schedule = schedule?.name

        binding.tvEmploymentType.text = getString(R.string.two_params, employment, schedule)
    }

    private fun viewCityOrRegion(area: FilterArea?, address: Address?) = with(binding) {
        val city = address?.city
        val region = area?.name

        if (!city.isNullOrEmpty()) {
            tvCompanyCity.text = city
        } else {
            tvCompanyCity.text = region
        }
    }

    private fun viewContacts(contacts: Contacts?) = with(binding) {
        val phonesList = contacts?.phones
        val phonesText = phonesList?.mapNotNull { it?.formatted }?.joinToString("\n")

        setupContactField(contacts?.email, tvEMail, eMail) { email ->
            tvEMail.text = email
            tvEMail.setOnClickListener {
                viewModel.emailTo(email)
            }
        }

        setupContactField(phonesText, tvNumberPhone, numberPhone) { phones ->
            tvNumberPhone.text = phones
            tvNumberPhone.setOnClickListener {
                phonesList?.firstOrNull { it?.formatted != null }?.formatted?.let(viewModel::callTo)
            }
        }

        setupContactField(contacts?.name, tvContactPerson, contactPerson) { name ->
            tvContactPerson.text = name
        }

        val comments = phonesList?.mapNotNull { it?.comment }?.filter { it.isNotBlank() }?.joinToString("\n")

        setupContactField(comments, tvComment, comment) { commentText ->
            tvComment.text = commentText
        }

        tvTitleContacts.showIf(
            listOf(
                contacts?.email,
                phonesText,
                contacts?.name,
                comments
            ).any { !it.isNullOrEmpty() }
        )
    }

    private fun setupContactField(
        value: String?,
        textView: TextView,
        container: View,
        setupAction: (String) -> Unit
    ) {
        val visible = !value.isNullOrEmpty()
        textView.showIf(visible)
        container.showIf(visible)
        if (visible) {
            setupAction(value)
        }
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        val item = binding.toolbar.menu.findItem(R.id.action_favorite)
        item.setIcon(
            if (isFavorite) {
                R.drawable.ic_favorites_on
            } else {
                R.drawable.ic_favorites_off
            }
        )
    }

    private fun viewCompanyLogo(vacancy: VacancyDetailed) {
        val cornerSize = resources.getDimensionPixelSize(R.dimen.corner_radius)
        val isNightMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK ==
            android.content.res.Configuration.UI_MODE_NIGHT_YES
        val placeholder = if (isNightMode) {
            R.drawable.ic_company_logo_placeholder_night
        } else {
            R.drawable.ic_company_logo_placeholder
        }
        Glide.with(this)
            .load(vacancy.employer?.logo)
            .placeholder(placeholder)
            .transform(RoundedCorners(cornerSize))
            .into(binding.icCompany)
    }

    private fun viewKeySkills(keySkills: List<KeySkill>?) {
        val keySkillsList = keySkills?.mapNotNull { it.name }
        val formattedKeySkills =
            if (keySkillsList.isNullOrEmpty()) "" else keySkillsList.joinToString("\n- ", prefix = "- ")

        binding.apply {
            if (formattedKeySkills.isEmpty()) {
                tvSkillsTitle.visibility = View.GONE
                tvSkills.visibility = View.GONE
            } else {
                tvSkillsTitle.visibility = View.GONE
                tvSkills.visibility = View.GONE
                tvSkills.text = formattedKeySkills
            }
        }
    }

    private fun renderError() {
        binding.errorContainer.visibility = View.VISIBLE
        binding.tvError.visibility = View.VISIBLE
        binding.ivError.visibility = View.VISIBLE
    }

    private fun getVacancyIdFromArgs() {
        val vacancyId = requireArguments().getString(ARG_VACANCY)

        if (!vacancyId.isNullOrEmpty()) {
            viewModel.loadVacancy(vacancyId)
        }
    }

    companion object {
        const val ARG_VACANCY = "vacancy_id"
    }
}

private fun View.show() {
    visibility = View.VISIBLE
}

private fun View.hide() {
    visibility = View.GONE
}

private fun View.showIf(condition: Boolean) {
    visibility = if (condition) {
        View.VISIBLE
    } else {
        View.GONE
    }
}
