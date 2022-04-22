package com.certified.covid19response.data.model

data class NewsApiOrgResponse(
    val status: String = "",
    val totalResults: Long = 0L,
    val articles: List<NewsApiOrgArticle> = listOf(),
    val code: String = "",
    val message: String = ""
)

data class NewsApiOrgArticle(
    val source: Source = Source(),
    val author: String = "",
    val title: String = "",
    val description: String = "",
    val url: String = "",
    val urlToImage: String = "",
    val publishedAt: String = "",
    val content: String = "",
)

data class Source(
    val id: String = "",
    val name: String = ""
)
