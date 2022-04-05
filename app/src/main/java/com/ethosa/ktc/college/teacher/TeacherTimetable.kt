package com.ethosa.ktc.college.teacher

/**
 * @param title week title
 * @param teacher teacher name
 * @param week days of week
 */
data class TeacherTimetable(
    val title: String,
    val teacher: String,
    val week: TeacherWeek
)
