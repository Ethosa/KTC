package com.ethosa.ktc

import android.content.Context
import com.ethosa.ktc.college.timetable.Branch
import com.ethosa.ktc.college.timetable.Group


/**
 * Provides working with SharedPreferences
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
        var week = 0
        var timetableState = 0
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
        isStudent = preferences.getBoolean(Constants.TIMETABLE_IS_STUDENT, true)
    }

    fun saveTimetable() {
        preferences.edit()
            .putInt(Constants.TIMETABLE_STATE, timetableState)
            .putString(Constants.TIMETABLE_GROUP_TITLE, group?.title)
            .putInt(Constants.TIMETABLE_GROUP, group!!.id)
            .putBoolean(Constants.TIMETABLE_IS_STUDENT, isStudent)
            .putInt(Constants.TIMETABLE_BRANCH, branch!!.id)
            .putInt(Constants.TIMETABLE_WEEK, week)
            .apply()
    }
}