package com.ethosa.ktc

/**
 * Constants class keeps all app constants
 */
sealed class Constants {
    companion object {
        const val PACKAGE = "com.ethosa.ktc"

        const val TIMETABLE_STATE = "state"
        const val TIMETABLE_BRANCH = "branch"
        const val TIMETABLE_GROUP = "group"
        const val TIMETABLE_GROUP_TITLE = "group_title"
        const val TIMETABLE_WEEK = "week"
        const val TIMETABLE_IS_STUDENT = "is_student"

        // when google services is available on phone.
        const val GOOGLE_PLAY_MARKET_URL = "market://details?id=com.ethosa.ktc"
        const val GOOGLE_PLAY_PACKAGE = "com.android.vending"
        // When google services isn't available on phone
        const val GITHUB_RELEASES_URL = "https://github.com/Ethosa/KTC/releases"
    }
}