package com.ethosa.ktc.college.teacher

import androidx.annotation.Keep

/**
 * @param title week title
 * @param teacher teacher name
 * @param week days of week
 */
@Keep
data class TeacherTimetable(
    val title: String,
    val teacher: String,
    val week: TeacherWeek
)
