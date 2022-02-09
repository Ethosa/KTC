package com.ethosa.ktc.college.objects.timetable

data class Lesson(
    val title: String = "",
    val teacher: String = "",
    val auditory: String = "",
    val timeFrom: String = "",
    val timeTo: String = "",
    val lessonNumber: Int = 1
)