package com.ethosa.ktc

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.ethosa.ktc.college.CollegeApi
import com.ethosa.ktc.college.CollegeCallback
import okhttp3.Call
import okhttp3.Response

import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class CollegeAPITest {
    private val college: CollegeApi = CollegeApi()

    /**
     * Testing last news from ktc.
     */
    @Test
    fun lastNews() {
        college.fetchLastNews(object : CollegeCallback {
            override fun onResponse(call: Call, response: Response) {
                println(response.body?.string())
            }
        })
    }
}