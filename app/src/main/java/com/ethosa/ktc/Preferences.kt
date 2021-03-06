package com.ethosa.ktc

import android.content.Context
import androidx.annotation.Keep
import com.ethosa.ktc.college.timetable.Branch
import com.ethosa.ktc.college.timetable.Group


/**
 * Provides working with SharedPreferences
 * @param context app context
 */
@Keep
class Preferences(
    context: Context
) {
    private val preferences = context.getSharedPreferences(
        Constants.PACKAGE,
        Context.MODE_PRIVATE
    )

    companion object {
        // timetable branch
        var branch: Branch = Branch(1, "")
        // student timetable
        var group: Group = Group(0, "")
        var isStudent = true
        var teacherId = 0
        var week = 0
        var timetableState = 0

        // Pro college
        var proCollegeUsername = ""
        var proCollegePassword = ""

        // App
        var currentTheme = "default"
        var currentFragment = 0
        var viewedNews = mutableSetOf<String>()
    }

    /**
     * Loads preferences
     */
    fun load() {
        with (preferences) {
            timetableState = getInt(Constants.TIMETABLE_STATE, 0)
            branch = Branch(
                getInt(Constants.TIMETABLE_BRANCH, 0),
                ""
            )
            group = Group(
                getInt(Constants.TIMETABLE_GROUP, 0),
                getString(Constants.TIMETABLE_GROUP_TITLE, "")!!
            )
            week = getInt(Constants.TIMETABLE_WEEK, 0)
            teacherId = getInt(Constants.TIMETABLE_TEACHER_ID, 0)
            isStudent = getBoolean(Constants.TIMETABLE_IS_STUDENT, true)

            proCollegeUsername = getString(Constants.LOGIN_USERNAME, "")!!
            proCollegePassword = getString(Constants.LOGIN_PASSWORD, "")!!

            currentTheme = getString(Constants.CURRENT_THEME, "default")!!
            currentFragment = getInt(Constants.CURRENT_FRAGMENT, 0)
            viewedNews = getStringSet(Constants.VIEWED_NEWS, mutableSetOf<String>())!!
            println(viewedNews)
        }
    }

    /**
     * Saves current timetable state
     */
    fun saveTimetable() {
        preferences.edit()
            .putInt(Constants.TIMETABLE_STATE, timetableState)
            .putString(Constants.TIMETABLE_GROUP_TITLE, group.title)
            .putInt(Constants.TIMETABLE_GROUP, group.id)
            .putBoolean(Constants.TIMETABLE_IS_STUDENT, isStudent)
            .putInt(Constants.TIMETABLE_BRANCH, branch.id)
            .putInt(Constants.TIMETABLE_WEEK, week)
            .putInt(Constants.TIMETABLE_TEACHER_ID, teacherId)
            .apply()
    }

    /**
     * Saves current pro college state
     */
    fun saveProCollege() {
        preferences.edit()
            .putString(Constants.LOGIN_USERNAME, proCollegeUsername)
            .putString(Constants.LOGIN_PASSWORD, proCollegePassword)
            .apply()
    }

    fun saveApp() {
        preferences.edit()
            .putInt(Constants.CURRENT_FRAGMENT, currentFragment)
            .putString(Constants.CURRENT_THEME, currentTheme)
            .putStringSet(Constants.VIEWED_NEWS, viewedNews.toSet())
            .apply()
        println(viewedNews)
    }
}