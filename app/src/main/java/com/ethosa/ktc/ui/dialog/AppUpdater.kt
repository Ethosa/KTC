package com.ethosa.ktc.ui.dialog

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.ethosa.ktc.BuildConfig
import com.ethosa.ktc.Constants
import com.ethosa.ktc.R
import com.ethosa.ktc.college.ActualAppVersion
import com.ethosa.ktc.college.CollegeApi
import com.ethosa.ktc.college.CollegeCallback
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Response


/**
 * AppUpdater class provides app updating
 */
class AppUpdater(
    private val context: AppCompatActivity
) {
    companion object {
        val VERSION = Array(3) {
            i -> BuildConfig.VERSION_NAME.split(".")[i].toInt()
        }
        val version = "v${VERSION[0]}.${VERSION[1]}.${VERSION[2]}"
        var omitted = "_omitted"

        var actualVersion: ActualAppVersion? = null
    }

    // For fetching actual version
    private val college = CollegeApi()
    // New version omitted
    private val preferences = context.getSharedPreferences(Constants.PACKAGE, Context.MODE_PRIVATE)
    private var updateOmitted = false


    private fun showDialog() {
        omitted = "${actualVersion!!}_omitted"
        updateOmitted = preferences.getBoolean(omitted, false)
        if (updateOmitted) return

        // Create dialog updater
        val dialog = MaterialAlertDialogBuilder(context)
            .setBackground(
                ResourcesCompat.getDrawable(
                    context.resources,
                    R.drawable.shape_background_secondary,
                    context.theme)
            )
            .setTitle(R.string.update_dialog_title)
            .setMessage(
                "Текущая версия: $version, актуальная версия: ${actualVersion!!}." +
                        "\n\nЧто нового:\n${actualVersion!!.description}")
            .setPositiveButton(R.string.update_dialog_positive) { dialog, _ ->
                try {
                    // Detect Google play market
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.GOOGLE_PLAY_MARKET_URL))
                    intent.`package` = Constants.GOOGLE_PLAY_PACKAGE
                    context.startActivity(intent)
                } catch (notFound: ActivityNotFoundException) {
                    // Go to GitHub releases
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Constants.GITHUB_RELEASES_URL))
                    context.startActivity(intent)
                }
                dialog.dismiss()
            }
            .setNeutralButton(R.string.update_dialog_neutral) { dialog, _ ->
                // Mark current actual version as omitted
                updateOmitted = true
                preferences.edit().putBoolean(omitted, true).apply()
                println(preferences.getBoolean(omitted, false))
                dialog.dismiss()
            }
            .setNegativeButton(R.string.update_dialog_negative) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        // Setup elevation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            dialog.window?.setElevation(16f)
            dialog.window?.setDimAmount(0.7f)
        } else {
            dialog.window?.setDimAmount(0.85f)
        }
        dialog.show()
    }

    /**
     * Fetches the actual app version.
     * If this version older than actual and isn't tagged as omitted than shows dialog.
     */
    fun checkToUpdate() {
        college.fetchActualVersion(object : CollegeCallback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                actualVersion = Gson().fromJson(body, ActualAppVersion::class.java)

//                if (!actualVersion!!.isActual())
                    context.runOnUiThread { showDialog() }
            }
        })
    }
}