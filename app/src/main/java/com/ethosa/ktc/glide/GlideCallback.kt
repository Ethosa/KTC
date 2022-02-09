package com.ethosa.ktc.glide

import android.graphics.Bitmap
import android.util.Log
import java.lang.Exception

interface GlideCallback {
    fun onReady(res: Bitmap)

    fun onFailure(e: Exception) {
        Log.e("GlideCallback", e.stackTraceToString())
    }
}