package com.certified.covid19response.data.model

data class News(
    val path: String = "",
    val title: String = "",
    val excerpt: String = "",
    val sourceUrl: String? = "",
    val webUrl: String = "",
    val originalUrl: String = "",
    val publishedDateTime: String = "",
    val provider: Provider = Provider(),
    val images: List<Image>
)

data class Image(
    val url: String = "",
    val width: Int = 0,
    val height: Int = 0,
    val title: String? = "",
    val attribution: String? = "",
    val isCached: Boolean = false
)