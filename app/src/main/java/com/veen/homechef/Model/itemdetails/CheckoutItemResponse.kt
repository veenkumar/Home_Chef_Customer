package com.veen.homechef.Model.itemdetails

data class CheckoutItemResponse(
    val `data`: CheckoutItemData,
    val msg: String,
    val status: Boolean
)