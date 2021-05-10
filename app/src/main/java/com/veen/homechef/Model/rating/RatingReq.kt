package com.veen.homechef.Model.rating

data class RatingReq(
    val chef_id: List<Int>,
    val order_id: String,
    val rating: MutableList<String>,
    val user_id: Int
)