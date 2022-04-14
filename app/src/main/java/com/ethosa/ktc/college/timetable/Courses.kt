package com.ethosa.ktc.college.timetable

import androidx.annotation.Keep

/**
 * [
 *      {
 *          "course": 1,
 *          "groups": [
 *              ...
 *          ]
 *      }, ...
 * ]
 */
@Keep
class Courses : ArrayList<Course>()
