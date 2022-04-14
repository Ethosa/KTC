package com.ethosa.ktc.college.teacher

import androidx.annotation.Keep

/**
 * @param number lesson number
 * @param title lesson title
 * @param classroom auditory
 * @param group group name
 */
@Keep
data class TeacherLesson(
    val number: String,
    val title: String,
    val classroom: String,
    val group: String
)
