package com.veen.homechef.Model.HomePage.menuitem

data class MenuItemData(
    val description: String,
    val id: String,
    val image: String,
    val name: String,
    val currency: String,
    val price: List<Price>
)