package com.ethosa.ktc.utils

import android.view.KeyEvent
import android.view.View
import androidx.fragment.app.Fragment

/**
 * Provides interface which includes onBackPressed function.
 */
abstract class IOFragmentBackPressed : Fragment() {
    /**
     * onBackPressed function should be called only when
     * user press on back button
     */
    abstract fun onBackPressed(): Boolean

    override fun onResume() {
        super.onResume()
        view?.isFocusableInTouchMode = true
        view?.requestFocus()
        view?.setOnKeyListener(View.OnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK)
                // return true if at branches
                return@OnKeyListener onBackPressed()
            return@OnKeyListener false
        })
    }
}