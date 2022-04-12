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
    companion object {
        const val DEFAULT_THEME = R.style.Theme_KTC_NoActionBar
        const val MATERIAL_THEME = R.style.Theme_KTC_MaterialYou
    }

    /**
     * Loads theme from preferences
     */
    fun loadTheme() {
        when(Preferences.currentTheme) {
            "default" ->
                if (Build.VERSION.SDK_INT >= 31)
                    context.setTheme(MATERIAL_THEME)
                else
                    context.setTheme(DEFAULT_THEME)
            else -> context.setTheme(DEFAULT_THEME)
        }
    }
}