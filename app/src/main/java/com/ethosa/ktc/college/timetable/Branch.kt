package com.ethosa.ktc.college.timetable

import androidx.annotation.Keep

/**
 * {
 *      "id": 1,
 *      "title": "Канский технологический колледж"
 * }
 */
@Keep
data class Branch(
    val id: Int,
    val title: String
)
