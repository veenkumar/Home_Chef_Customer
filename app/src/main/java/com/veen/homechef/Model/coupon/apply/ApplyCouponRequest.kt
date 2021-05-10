package com.veen.homechef.Model.coupon.apply

data class ApplyCouponRequest(
    val chef_id: Int,
    val coupon_id: Int,
    val user_id: Int
)