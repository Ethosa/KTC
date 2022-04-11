package com.ethosa.ktc

import android.content.Context
import com.ethosa.ktc.college.timetable.Branch
import com.ethosa.ktc.college.timetable.Group


/**
 * Provides working with SharedPreferences
 * @param context app context
 */
class Preferences(
    context: Context
) {
    private val preferences = context.getSharedPreferences(
        Constants.PACKAGE,
        Context.MODE_PRIVATE
    )

    companion object {
        // timetable branch
        var branch: Branch? = null
        // student timetable
        var group: Group? = null
        var isStudent = true
        var teacherId = 0
        var week = 0
        var timetableState = 0

        // Pro college
        var proCollegeUsername = ""
        var proCollegePassword = ""

        // AppDynamicTheme
        var currentTheme = "default"
    }

    /**
     * Loads preferences
     */
    fun load() {
        timetableState = preferences.getInt(Constants.TIMETABLE_STATE, 0)
        branch = Branch(
            preferences.getInt(Constants.TIMETABLE_BRANCH, 0),
            ""
        )
        group = Group(
            preferences.getInt(Constants.TIMETABLE_GROUP, 0),
            preferences.getString(Constants.TIMETABLE_GROUP_TITLE, "")!!
        )
        week = preferences.getInt(Constants.TIMETABLE_WEEK, 0)
        teacherId = preferences.getInt(Constants.TIMETABLE_TEACHER_ID, 0)
        isStudent = preferences.getBoolean(Constants.TIMETABLE_IS_STUDENT, true)

        proCollegeUsername = preferences.getString(Constants.LOGIN_USERNAME, "")!!
        proCollegePassword = preferences.getString(Constants.LOGIN_PASSWORD, "")!!
        currentTheme = preferences.getString(Constants.CURRENT_THEME, "default")!!
    }

    /**
     * Clears current timetable state
     */
    fun clearTimetable() {
        preferences.edit()
            .putInt(Constants.TIMETABLE_STATE, 0)
            .putString(Constants.TIMETABLE_GROUP_TITLE, "")
            .putInt(Constants.TIMETABLE_GROUP, 0)
            .putBoolean(Constants.TIMETABLE_IS_STUDENT, true)
            .putInt(Constants.TIMETABLE_BRANCH, 0)
            .putInt(Constants.TIMETABLE_WEEK, 0)
            .putInt(Constants.TIMETABLE_TEACHER_ID, 0)
            .apply()
    }

    /**
     * Clears current pro college state
     */
    fun clearProCollege() {
        preferences.edit()
            .putString(Constants.LOGIN_USERNAME, "")
            .putString(Constants.LOGIN_PASSWORD, "")
            .apply()
    }

    /**
     * Saves current timetable state
     */
    fun saveTimetable() {
        preferences.edit()
            .putInt(Constants.TIMETABLE_STATE, timetableState)
            .putString(Constants.TIMETABLE_GROUP_TITLE, group?.title)
            .putInt(Constants.TIMETABLE_GROUP, group!!.id)
            .putBoolean(Constants.TIMETABLE_IS_STUDENT, isStudent)
            .putInt(Constants.TIMETABLE_BRANCH, branch!!.id)
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

    fun saveTheme() {
        preferences.edit()
            .putString(Constants.CURRENT_THEME, currentTheme)
            .apply()
    }
}