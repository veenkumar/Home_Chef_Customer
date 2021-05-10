package com.veen.homechef.Model.HomePage.menu

data class MenuData(
    val chef_image: String,
    val food_image: ArrayList<FoodImage>,
    val id: String,
    val name: String
)