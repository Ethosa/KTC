package com.ethosa.ktc.college.news

import androidx.annotation.Keep

/**
 * List of last news.
 */
@Keep
data class LastNewsBeta(
    val announce: List<News>,
    val news: List<News>
)

