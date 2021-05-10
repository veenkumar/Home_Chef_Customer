package com.veen.homechef.Model.Cart.CartView

data class Data(
    val cart_data: List<CartData>,
    val currency: String,
    val total_amount: Int
)