package com.certified.covid19response.data.model

data class RapidApiResponse(
    val location: Location = Location(),
    val updatedDateTime: String = "",
    val news: List<News>
)

data class Location(
    val long: Double = 0.0,
    val countryOrRegion: String = "",
    val provinceOrState: String = "",
    val county: String = "",
    val isoCode: String = "",
    val lat: Double = 0.0
)