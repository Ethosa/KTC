package com.ethosa.ktc.college.timetable

/**
 * {
 *      "title": "Проектирование и дизайн информационных систем",
 *      "teacher": "Гринь Д.Х.",
 *      "auditory": "3.10",
 *      "time": ["1", "8:15", "9:45"]
 * }
 */
data class Lesson(
    val title: String = "",
    val teacher: String = "",
    val auditory: String = "",
    val time: LessonTime
)