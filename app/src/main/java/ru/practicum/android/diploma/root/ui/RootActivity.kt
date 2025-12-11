package ru.practicum.android.diploma.root.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityRootBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.containerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateBottomNavVisibility(destination.id)
        }

        // Отслеживаем изменения layout для предотвращения появления BottomNavBar при появлении клавиатуры
        binding.root.viewTreeObserver.addOnGlobalLayoutListener {
            navController.currentDestination?.id?.let { destinationId ->
                updateBottomNavVisibility(destinationId)
            if (
                destination.id == R.id.filtersFragment ||
                destination.id == R.id.filterIndustryFragment
            ) {
                binding.bottomNavigationView.visibility = android.view.View.GONE
            } else {
                binding.bottomNavigationView.visibility = android.view.View.VISIBLE
            }
        }
    }

        // Если на экране фильтра или отрасли появилась клавиатура, то не показываем BottomNavBar

    private fun updateBottomNavVisibility(destinationId: Int) {
        val shouldHide = destinationId == R.id.filtersFragment ||
            destinationId == R.id.filterIndustryFragment
        binding.bottomNavigationView.visibility = if (shouldHide) {
            android.view.View.GONE
        } else {
            android.view.View.VISIBLE
        }
    }
}
