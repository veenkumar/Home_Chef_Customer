package com.veen.homechef.Model.HomePage.subcategory

data class MenuSubResponse(
    val `data`: List<MenuSubData>,
    val msg: String,
    val status: Boolean
)