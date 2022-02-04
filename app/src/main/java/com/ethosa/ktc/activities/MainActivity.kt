package com.ethosa.ktc.activities

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.ethosa.ktc.R
import com.ethosa.ktc.college.CollegeApi
import com.ethosa.ktc.college.CollegeCallback
import com.ethosa.ktc.databinding.ActivityMainBinding
import okhttp3.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var college: CollegeApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        college = CollegeApi()
        setContentView(binding.root)
        setupUi()
    }

    private fun setupUi() {
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_timetable
            ))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        college.lastNews(object: CollegeCallback<Response> {
            override fun onResponse(response: Response) {
                println(response.body?.string())
            }
        })
    }
}