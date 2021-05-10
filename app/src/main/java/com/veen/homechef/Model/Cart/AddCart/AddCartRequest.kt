package com.veen.homechef.Model.Cart.AddCart

data class AddCartRequest(
    val chef_id: Int,
    val item_id: Int,
    val plate_size: String,
    val price: String,
    val qty: String,
    val user_id: Int
)