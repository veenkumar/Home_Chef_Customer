package com.veen.homechef.Model.HomePage.menu

data class MenuResponse(
    val `data`: List<MenuData>,
    val msg: String,
    val status: Boolean
)