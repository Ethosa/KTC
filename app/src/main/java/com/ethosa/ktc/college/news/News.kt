package com.ethosa.ktc.college.news

/**
 * {
 *      "title": "Молодежная площадка «Поколение 2030»",
 *      "body": "Студенты нашего колледжа примут участие  в Молоде...",
 *      "date": "21 Фев",
 *      "image": "http://...jpg",
 *      "id": "2392"
 * }
 */
data class News(
    val title: String,
    var body: String,
    val date: String,
    val image: String,
    val id: String
)
