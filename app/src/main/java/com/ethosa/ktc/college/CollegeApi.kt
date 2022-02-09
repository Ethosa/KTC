package com.ethosa.ktc.college

import com.ethosa.ktc.college.interfaces.CollegeCallback
import okhttp3.OkHttpClient
import okhttp3.Request

class CollegeApi {
    companion object {
        private var client: OkHttpClient = OkHttpClient()
    }

    /**
     * Gets the last news and receives it in onResponse method
     */
    fun lastNews(callback: CollegeCallback) {
        val request: Request.Builder = Request.Builder()
            .get()
            .url("http://api.kansk-tc.ru/news/")

        client.newCall(request.build()).enqueue(callback)
    }

    /**
     * Fetches the new by ID.
     */
    fun fetchNewById(id: Int, callback: CollegeCallback) {
        val request: Request.Builder = Request.Builder()
            .get()
            .url("http://api.kansk-tc.ru/news/$id")

        client.newCall(request.build()).enqueue(callback)
    }
}