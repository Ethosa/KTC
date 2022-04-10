package com.ethosa.ktc.utils

import android.content.Context
import android.os.Build
import com.ethosa.ktc.Preferences
import com.ethosa.ktc.R

/**
 * @param context application context
 */
class AppDynamicTheme(
    private val context: Context
) {
    /**
     * Loads theme from preferences
     */
    fun loadTheme() {
        when(Preferences.currentTheme) {
            "default" ->
                if (Build.VERSION.SDK_INT >= 31)
                    context.setTheme(R.style.Theme_KTC_MaterialYou)
                else
                    context.setTheme(R.style.Theme_KTC_NoActionBar)
            else -> context.setTheme(R.style.Theme_KTC_NoActionBar)
        }
    }
}