package com.ethosa.ktc.ui.activities

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Preferences(this).load()
        AppDynamicTheme(this).loadTheme()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        AppUpdater(this).checkToUpdate()
    }
}