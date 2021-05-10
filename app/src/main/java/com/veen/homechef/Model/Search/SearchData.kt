package com.veen.homechef.Model.Search

data class SearchData(
    val chef_id: String,
    val chef_name: String,
    val description: String,
    val food_name: String,
    val id: String,
    val image: String,
    val currency: String,
    val price: List<SearchPrice>
)