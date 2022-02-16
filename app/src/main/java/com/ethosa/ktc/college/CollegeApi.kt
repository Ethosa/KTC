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

        private const val MY_API: String = "http://109.248.133.17:8000/ktc-api"
        private const val BRANCHES: String = "$MY_API/branches"
        private const val COURSES: String = "$MY_API/courses"
        private const val TIMETABLE: String = "$MY_API/timetable"

        fun sendRequest(url: String, callback: CollegeCallback) {
            val request: Request.Builder = Request.Builder()
                .get()
                .url(url)
            client.newCall(request.build()).enqueue(callback)
        }
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
     * Fetches all college branches
     */
    fun fetchBranches(callback: CollegeCallback) {
        sendRequest(BRANCHES, callback)
    }

    /**
     * Fetch all branch courses
     */
    fun fetchCourses(branchId: String, callback: CollegeCallback) {
        sendRequest("$COURSES/$branchId", callback)
    }

    /**
     * Gets the last news and receives it in onResponse method
     */
    fun fetchLastNews(callback: CollegeCallback) {
        sendRequest(NEWS_API, callback)
    }

    /**
     * Fetches the new by ID.
     */
    fun fetchNewById(id: Int, callback: CollegeCallback) {
        sendRequest("$NEWS_API$id", callback)
    }

    /**
     * Fetches the timetable for specified group and week.
     * @param groupId course group ID.
     * @param week week number. by default fetches current week.
     */
    fun fetchTimetable(groupId: String, callback: CollegeCallback, week: Int? = null) {
        if (week == null)
            sendRequest("$TIMETABLE/$groupId", callback)
        else
            sendRequest("$TIMETABLE/$groupId/$week", callback)
    }
}