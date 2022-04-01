package com.ethosa.ktc.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ethosa.ktc.R
import com.ethosa.ktc.databinding.ActivityMainBinding
import com.ethosa.ktc.utils.AppUpdater
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * The main app activity.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        val appUpdater = AppUpdater(this)
        appUpdater.checkToUpdate()
    }
}