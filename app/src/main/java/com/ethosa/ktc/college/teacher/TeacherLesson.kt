package com.ethosa.ktc.college.teacher

/**
 * @param number lesson number
 * @param title lesson title
 * @param classroom auditory
 * @param group group name
 */
data class TeacherLesson(
    val number: String,
    val title: String,
    val classroom: String,
    val group: String
)
