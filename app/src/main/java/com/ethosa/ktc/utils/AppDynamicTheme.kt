package com.ethosa.ktc.utils

import android.content.Context
import com.ethosa.ktc.Preferences
import com.ethosa.ktc.R

/**
 * @param context application context
 */
class AppDynamicTheme(
    private val context: Context
) {
    fun loadTheme() {
        when(Preferences.currentTheme) {
            "default" -> context.setTheme(R.style.Theme_KTC_NoActionBar)
            "material3" -> context.setTheme(R.style.Theme_KTC_MaterialYou)
            else -> context.setTheme(R.style.Theme_KTC_NoActionBar)
        }
    }

    fun changeTheme(newTheme: String) {
        Preferences.currentTheme = newTheme
    }
}