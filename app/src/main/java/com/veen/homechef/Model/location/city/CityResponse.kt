package com.veen.homechef.Model.location.city

data class CityResponse(
    val `data`: List<CityData>,
    val msg: String,
    val status: Boolean
)