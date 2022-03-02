package com.ethosa.ktc.college.timetable

/**
 * {
 *      "title": "19 февраля, Суббота",
 *      "lessons":
 *          [{
 *              "title": "Проектирование и дизайн информационных систем",
 *              "teacher": "Гринь Д.Х.",
 *              "classroom": "3.10",
 *              "time": ["1", "8:15", "9:45"]
 *          }, ...]
 * }
 */
data class Day(
    val lessons: List<Lesson>,
    val title: String
)
