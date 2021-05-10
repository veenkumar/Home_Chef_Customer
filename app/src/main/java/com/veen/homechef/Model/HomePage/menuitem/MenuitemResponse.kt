package com.veen.homechef.Model.HomePage.menuitem

data class MenuitemResponse(
    val `data`: List<MenuItemData>,
    val msg: String,
    val status: Boolean
)