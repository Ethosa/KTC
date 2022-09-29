package com.ethosa.ktc.college.news

import androidx.annotation.Keep

/**
 * {
 *      "title": "Молодежная площадка «Поколение 2030»",
 *      "body": "Студенты нашего колледжа примут участие  в Молоде...",
 *      "date": "21 Фев",
 *      "image": "http://...jpg",
 *      "id": "2392"
 * }
 */
@Keep
data class News(
    val title: String,
    var body: String,
    val date: String,
    var image: String,
    val id: String
)