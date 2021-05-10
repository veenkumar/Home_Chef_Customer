package com.veen.homechef.Model.Profile.viewdetails

data class ViewDetailsItemData(
    val food_name: String,
    val id: String,
    val image: String,
    val order_status: String,
    val order_status_type: String,
    val plate_size: String,
    val price: String,
    val qty: String,
    val subtotal: Int
)