package com.ethosa.ktc.college.news

import androidx.annotation.Keep

/**
 * List of last news.
 */
@Keep
data class LastNews(
    val anonce: List<News>,
    val news: List<News>
)
