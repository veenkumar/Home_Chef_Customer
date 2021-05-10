package com.veen.homechef.Model.itemdetails

data class CheckoutItemItem(
    val currency: String,
    val food_name: String,
    val id: String,
    val item_subtotal: Int,
    val plate_size_name: String,
    val price: String,
    val qty: String
)