package com.veen.homechef.Model.itemdetails

data class CheckoutItemData(
    val currency: String,
    val item_list: List<CheckoutItemItem>,
    val shipping_charge: Int,
    val subtotal: Int,
    val total: Int,
    val discount: Int,
    val coupon_status: Int
)