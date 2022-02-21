package com.ethosa.ktc.college.interfaces

import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import java.io.IOException

/**
 * Wrapper around okhttp3.Callback
 */
interface CollegeCallback : Callback {
    /**
     * Calls on request failure
     */
    override fun onFailure(call: Call, e: IOException) {
        Log.e("CollegeAPI", e.message.toString())
    }
}