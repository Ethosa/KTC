package com.ethosa.ktc.college.objects.timetable

/**
 * {
 *      "course": 1,
 *      "groups": [
 *          {
 *              "id": 264,
 *              "title": "РП.09.21.1"
 *          }, ...
 *      ]
 * }
 */
data class Course(
    val course: Int,
    val groups: Groups
)
