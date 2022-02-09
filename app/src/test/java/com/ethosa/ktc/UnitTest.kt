package com.ethosa.ktc

import com.ethosa.ktc.college.CollegeApi
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UnitTest {
    @Test
    fun lastNews() {
        val api = CollegeApi()
        api.lastNews()
    }
}