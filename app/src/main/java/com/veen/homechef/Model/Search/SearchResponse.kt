package com.veen.homechef.Model.Search

data class SearchResponse(
    val `data`: List<SearchData>,
    val msg: String,
    val status: Boolean
)