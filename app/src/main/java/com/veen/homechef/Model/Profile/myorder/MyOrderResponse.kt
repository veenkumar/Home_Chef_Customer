package com.veen.homechef.Model.Profile.myorder

data class MyOrderResponse(
    val `data`: List<MyOrderData>,
    val msg: String,
    val status: Boolean
)