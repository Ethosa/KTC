package com.ethosa.ktc.college

import com.ethosa.ktc.college.interfaces.CollegeCallback
import okhttp3.OkHttpClient
import okhttp3.Request

class CollegeApi {
    companion object {
        private val client: OkHttpClient = OkHttpClient()

        private const val API_URL: String = "http://api.kansk-tc.ru/"
        private const val NEWS_API: String = "${API_URL}news/"
        private const val ALBUMS_API: String = "${API_URL}albums/"

        fun sendRequest(url: String, callback: CollegeCallback) {
            val request: Request.Builder = Request.Builder()
                .get()
                .url(url)

            client.newCall(request.build()).enqueue(callback)
        }
    }

    /**
     * Gets the last news and receives it in onResponse method
     */
    fun lastNews(callback: CollegeCallback) {
        sendRequest(NEWS_API, callback)
    }

    /**
     * Fetches all albums
     */
    fun fetchAlbums(callback: CollegeCallback) {
        sendRequest(ALBUMS_API, callback)
    }

    /**
     * Fetches the new by ID.
     */
    fun fetchAlbumById(id: String, callback: CollegeCallback) {
        sendRequest("$ALBUMS_API$id", callback)
    }

    /**
     * Fetches the new by ID.
     */
    fun fetchNewById(id: Int, callback: CollegeCallback) {
        sendRequest("$NEWS_API$id", callback)
    }
}