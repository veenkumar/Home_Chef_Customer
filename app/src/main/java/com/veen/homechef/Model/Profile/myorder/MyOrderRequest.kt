package com.veen.homechef.Model.Profile.myorder

data class MyOrderRequest(
    val end_date: String,
    val order_id: String,
    val start_date: String,
    val user_id: Int
)