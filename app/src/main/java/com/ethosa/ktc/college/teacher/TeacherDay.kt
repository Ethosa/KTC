package com.ethosa.ktc.college.teacher

import androidx.annotation.Keep

/**
 * @param title day title
 * @param lessons day lessons
 */
@Keep
data class TeacherDay(
    val title: String,
    val lessons: ArrayList<TeacherLesson>
)
