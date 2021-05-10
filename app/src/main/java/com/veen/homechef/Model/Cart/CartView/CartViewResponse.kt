package com.veen.homechef.Model.Cart.CartView

data class CartViewResponse(
    val `data`: List<Data>,
    val msg: String,
    val status: Boolean
)