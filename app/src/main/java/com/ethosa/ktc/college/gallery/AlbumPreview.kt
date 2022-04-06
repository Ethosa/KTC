package com.ethosa.ktc.college.gallery

/**
 * @param id album ID
 * @param title album title
 * @param preview album preview image URL
 * @param date album addition date
 */
data class AlbumPreview(
    val id: String,
    val title: String,
    val preview: String,
    val date: String
)
