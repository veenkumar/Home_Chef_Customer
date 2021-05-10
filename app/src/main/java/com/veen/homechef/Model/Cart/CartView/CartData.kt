package com.veen.homechef.Model.Cart.CartView

data class CartData(
    val currency: String,
    val food_name: String,
    val id: String,
    val plate_size_name: String,
    val price: String,
    val qty: String,
    val subtotal: Int,
    val image: String
)