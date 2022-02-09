package com.ethosa.ktc

import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ethosa.ktc.college.CollegeApi
import com.ethosa.ktc.college.interfaces.CollegeCallback
import okhttp3.Call
import okhttp3.Response

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class CollegeAPITest {
    private lateinit var appContext: Context
    private lateinit var college: CollegeApi

    @Test
    fun appContext() {
        // Context of the app under test.
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.ethosa.ktc", appContext.packageName)
    }

    @Test
    fun initCollege() {
        college = CollegeApi()
    }

    @Test
    fun lastNews() {
        college.lastNews(object : CollegeCallback {
            override fun onResponse(call: Call, response: Response) {
                println(response.body?.string())
            }
        })
    }
}