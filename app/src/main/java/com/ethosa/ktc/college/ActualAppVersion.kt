package com.ethosa.ktc.college

import androidx.annotation.Keep
import com.ethosa.ktc.ui.dialog.AppUpdater

/**
 * JSON:
 *  {
 *      "actual_version": [0, 6, 0],
 *      "description": "update description"
 *  }
 *
 *  @param actual_version list of major, minor and patch version.
 *  @param description update description
 */
@Keep
data class ActualAppVersion(
    val actual_version: List<Int>,
    val description: String
) {
    /**
     * Check to version actual.
     * @return true if version is actual.
     */
    fun isActual(): Boolean {
        for (i in 0..2) {
            if (actual_version[i] > AppUpdater.VERSION[i]) {
                return false
            }
        }
        return true
    }

    override fun toString(): String {
        return "v${actual_version[0]}.${actual_version[1]}.${actual_version[2]}"
    }
}
