package com.veen.homechef.Model.Cart.update

data class CartUpdateRequest(
    val id: Int,
    val qty: Int,
    val user_id: String
)