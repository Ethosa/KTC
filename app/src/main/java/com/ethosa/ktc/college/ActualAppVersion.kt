package com.ethosa.ktc.college

/**
 * JSON:
 *  { "actual_version": [0, 6, 0] }
 */
data class ActualAppVersion(
    val actual_version: List<Int>,
    val download_url: String
) {
    fun isActual(): Boolean {
        for (i in 0..2) {
            if (actual_version[i] > CollegeApi.VERSION[i]) {
                return false
            }
        }
        return true
    }
}
