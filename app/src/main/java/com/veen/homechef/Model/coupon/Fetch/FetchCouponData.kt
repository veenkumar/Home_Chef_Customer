package com.veen.homechef.Model.coupon.Fetch

data class FetchCouponData(
    val chef_id: String,
    val coupopn_code: String,
    val currency: String,
    val description: String,
    val discount: String,
    val discount_type: String,
    val id: String,
    val minimum_price_range: String,
    val title: String
)