package com.ethosa.ktc.glide

import android.graphics.Bitmap
import android.util.Log
import java.lang.Exception

/**
 * Provides little Glide callback
 */
interface GlideCallback {
    /**
     * Should be called when resource was loaded.
     */
    fun onReady(res: Bitmap)

    /**
     * Should be called on failure resource load.
     */
    fun onFailure(e: Exception) {
        Log.e("GlideCallback", e.stackTraceToString())
    }
}