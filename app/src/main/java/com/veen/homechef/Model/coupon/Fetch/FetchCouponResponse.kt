package com.veen.homechef.Model.coupon.Fetch

data class FetchCouponResponse(
    val `data`: List<FetchCouponData>,
    val msg: String,
    val status: Boolean
)