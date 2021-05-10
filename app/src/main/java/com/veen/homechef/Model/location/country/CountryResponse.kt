package com.veen.homechef.Model.location.country

data class CountryResponse(
    val `data`: List<CountryData>,
    val msg: String,
    val status: Boolean
)