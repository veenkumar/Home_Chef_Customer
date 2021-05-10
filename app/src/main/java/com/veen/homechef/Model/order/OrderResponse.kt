package com.veen.homechef.Model.order

data class OrderResponse(
    val msg: String,
    val status: Boolean,
    val order_id: Int
)