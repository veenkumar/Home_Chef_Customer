package com.veen.homechef.Model.order

data class OrderRequest(
    val address: String,
    val current_location: String,
    val email: String,
    val name: String,
    val payment_type: Int,
    val phone: String,
    val pincode: String,
    val user_id: Int
)