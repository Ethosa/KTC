package com.ethosa.ktc.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ethosa.ktc.R
import com.ethosa.ktc.college.ActualAppVersion
import com.ethosa.ktc.college.CollegeApi
import com.ethosa.ktc.college.CollegeCallback
import com.ethosa.ktc.databinding.ActivityMainBinding
import com.ethosa.ktc.utils.AppUpdater
import com.ethosa.ktc.utils.Permissions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Response

/**
 * The main app activity.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val collegeApi = CollegeApi()
    private val permissions = Permissions()

    companion object {
        const val PERMISSION_STORAGE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        // Check to actual version
        collegeApi.fetchActualVersion(object : CollegeCallback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val actual = Gson().fromJson(body, ActualAppVersion::class.java)
                val newVersion = "v${actual.actual_version[0]}.${actual.actual_version[1]}.${actual.actual_version[2]}"

                if (actual.isActual())
                    return
                runOnUiThread {
                    MaterialAlertDialogBuilder(this@MainActivity)
                        .setTitle("Доступно обновление")
                        .setMessage("Текущая версия: ${CollegeApi.version}, Актуальная: $newVersion.")
                        .setNegativeButton("Не сейчас") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .setPositiveButton("Скачать обновление") { dialog, _ ->
                            permissions.requestPermissions(this@MainActivity, PERMISSION_STORAGE)
                            if (!permissions.hasPermissions(this@MainActivity))
                                return@setPositiveButton
                            AppUpdater(this@MainActivity).update(actual.download_url)
                            dialog.dismiss()
                        }
                        .show()
                }
            }
        })
    }
}