package ru.practicum.android.diploma.root.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityRootBinding.inflate(layoutInflater)
    }

    // Флаг для отслеживания видимости клавиатуры
    private var isKeyboardVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.containerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        // Слушатель для отслеживания видимости клавиатуры
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime())
            if (imeVisible != isKeyboardVisible) {
                isKeyboardVisible = imeVisible
                updateBottomNavVisibility(navController.currentDestination?.id ?: 0)
            }
            insets
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateBottomNavVisibility(destination.id)
        }
    }

    private fun updateBottomNavVisibility(destinationId: Int) {
        // Скрываем BottomNavBar если:
        // 1. Это экран фильтров или отрасли
        // 2. Клавиатура видна (во время поиска или ввода)
        val shouldHide = isKeyboardVisible ||
            destinationId == R.id.filtersFragment ||
            destinationId == R.id.filterIndustryFragment

        binding.bottomNavigationView.visibility = if (shouldHide) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}
