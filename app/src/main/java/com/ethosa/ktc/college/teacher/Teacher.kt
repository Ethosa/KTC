package com.ethosa.ktc.college.teacher

import androidx.annotation.Keep

/**
 * JSON is
 * {
 *      "id": "2388",
 *      "name": "Asd Asd"
 * }
 */
@Keep
data class Teacher(
    val id: Int,
    val name: String
)