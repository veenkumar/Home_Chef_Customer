package com.veen.homechef.Model.chef

data class ChefRes(
    val `data`: List<ChefData>,
    val msg: String,
    val status: Boolean
)