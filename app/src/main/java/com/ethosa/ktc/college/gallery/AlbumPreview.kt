package com.ethosa.ktc.college.gallery

import androidx.annotation.Keep

/**
 * @param id album ID
 * @param title album title
 * @param preview album preview image URL
 * @param date album addition date
 */
@Keep
data class AlbumPreview(
    val id: String,
    val title: String,
    val preview: String,
    val date: String
)
