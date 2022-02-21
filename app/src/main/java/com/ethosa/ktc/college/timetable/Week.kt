package com.ethosa.ktc.college.timetable

/**
 * {
 *    "days": [...],
 *    "week_number": 1
 * }
 */
data class Week(
    val days: List<Day>,
    val week_number: Int
)
