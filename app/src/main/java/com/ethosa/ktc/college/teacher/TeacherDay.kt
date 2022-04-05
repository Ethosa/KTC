package com.ethosa.ktc.college.teacher

/**
 * @param title day title
 * @param lessons day lessons
 */
data class TeacherDay(
    val title: String,
    val lessons: ArrayList<TeacherLesson>
)
