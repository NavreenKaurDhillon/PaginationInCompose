package com.example.demopaginationapp.model.dataclasses

data class Data(
    val cartItemsCount: String,
    val categories: List<Category>,
    val currencyId: String,
    val currencySymbol: String,
    val totalFavouriteItems: String,
    val totalUnreadMessageCount: String,
    val totalUnreadNotificationCount: String
)