package com.ethosa.ktc.ui.activities

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ethosa.ktc.Preferences
import com.ethosa.ktc.R
import com.ethosa.ktc.databinding.ActivityMainBinding
import com.ethosa.ktc.ui.dialog.AppUpdater
import com.ethosa.ktc.utils.AppDynamicTheme

/**
 * The main app activity.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Load adaptive or not theme
        preferences = Preferences(this)
        preferences.load()
        AppDynamicTheme(this).loadTheme()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= 31)
            binding.navView.itemActiveIndicatorColor = getColorStateList(R.color.active_indicator)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        binding.navView.setupWithNavController(navController)

        // Navigate to the last menu
        binding.navView.setOnItemSelectedListener {
            Preferences.currentFragment = getMenuNumber(it.itemId)
            navController.navigate(getMenuId())
            preferences.saveFragment()
            true
        }
        binding.navView.selectedItemId = getMenuId()

        // Check to updates
        AppUpdater(this).checkToUpdate()
    }

    /**
     * @return saved menu unique ID.
     */
    private fun getMenuId(): Int {
        return when (Preferences.currentFragment) {
            0 -> R.id.navigation_news
            1 -> R.id.navigation_timetable
            2 -> R.id.navigation_gallery
            else -> R.id.navigation_account
        }
    }

    /**
     * @param id unique menu ID.
     * @return menu number by it's ID.
     */
    private fun getMenuNumber(id: Int): Int {
        return when (id) {
            R.id.navigation_news -> 0
            R.id.navigation_timetable -> 1
            R.id.navigation_gallery -> 2
            else -> 3
        }
    }
}