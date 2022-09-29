package com.ethosa.ktc.college

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import android.provider.Settings.*
import androidx.annotation.Keep
import com.ethosa.ktc.ui.dialog.AppUpdater
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

/**
 * Provides working with KTC api.
 */
@Keep
class CollegeApi {
    companion object {
        private val client = OkHttpClient()

        private const val API_URL = "http://api.kansk-tc.ru/"
        private const val ALBUMS_API = "${API_URL}albums/"

        private const val MY_API = "http://mob.kansk-tc.ru/ktc-api"
        private const val BRANCHES = "$MY_API/branches"
        private const val COURSES = "$MY_API/courses"
        private const val TIMETABLE = "$MY_API/timetable"
        private const val TEACHER_TIMETABLE = "$MY_API/teacher-timetable"
        private const val TEACHERS_LIST = "$MY_API/teachers-list"
        private const val ACTUAL_VERSION = "$MY_API/actual-version"
        private const val LAST_NEWS = "$MY_API/news/"
        private const val NEWS_BY_ID = "$MY_API/news/id"
        private const val LIKE = "$MY_API/news/like"

        /**
         * Sends GET request to url.
         */
        private fun sendRequest(url: String, callback: CollegeCallback) {
            val request: Request.Builder = Request.Builder()
                .get()
                .url(url)
            client.newCall(request.build()).enqueue(callback)
        }

        /**
         * Fetches JSON with actual app version.
         */
        fun fetchActualVersion(callback: CollegeCallback) {
            sendRequest(ACTUAL_VERSION, callback)
        }

        /**
         * Fetches all albums
         */
        fun fetchAlbums(callback: CollegeCallback) {
            sendRequest(ALBUMS_API, callback)
        }

        /**
         * Fetches the album by ID.
         * @param id unique album ID.
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
         * @param branchId unique branch ID.
         */
        fun fetchCourses(branchId: Int, callback: CollegeCallback) {
            sendRequest("$COURSES/$branchId", callback)
        }

        /**
         * Fetches the timetable for specified group and week.
         * @param groupId course group ID.
         * @param week week number. by default fetches current week.
         */
        fun fetchTimetable(groupId: Int, callback: CollegeCallback, week: Int? = null) {
            if (week == null)
                sendRequest("$TIMETABLE/$groupId", callback)
            else
                sendRequest("$TIMETABLE/$groupId/$week", callback)
        }

        /**
         * Fetches the teachers list in branch.
         * @param branchId branch ID.
         */
        fun fetchTeachersList(branchId: Int, callback: CollegeCallback) {
            sendRequest("$TEACHERS_LIST/$branchId", callback)
        }

        /**
         * Fetches the teacher's timetable.
         * @param branchId branch ID.
         * @param teacherId unique teacher ID.
         */
        fun fetchTeacherTimetable(branchId: Int, teacherId: Int, callback: CollegeCallback) {
            sendRequest("$TEACHER_TIMETABLE/$branchId/$teacherId", callback)
        }

        /**
         * Fetches last news from new Public Beta API
         * @param callback College callback interface
         */
        fun fetchLastNewsBeta(callback: CollegeCallback) {
            sendRequest(LAST_NEWS, callback)
        }

        /**
         * Fetches news by ID from new Public Beta API
         * @param id news ID
         * @param callback College callback interface
         */
        fun fetchNewsByIdBeta(id: Int, callback: CollegeCallback) {
            sendRequest("$NEWS_BY_ID$id", callback)
        }

        @SuppressLint("HardwareIds")
        fun likeNewsById(id: Int, ctx: Context) {
            sendRequest(
                "$LIKE$id?uuid=${AppUpdater.UUID}",
                object: CollegeCallback {
                    override fun onResponse(call: Call, response: Response) {
                        println(response.body?.string())
                    }
                }
            )
        }
    }
}