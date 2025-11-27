package com.example.demopaginationapp.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import com.example.demopaginationapp.model.dataclasses.BottomNavItem
import com.example.demopaginationapp.navigation.Screens

object Constants {
    val BottomNavItems = listOf(
        // Home screen
        BottomNavItem(
            tabName = "Home",
            tabIcon = Icons.Filled.Home,
            destination = Screens.Home
        ),
        // Search screen
        BottomNavItem(
            tabName = "Products",
            tabIcon = Icons.Filled.List,
            destination = Screens.ProductsList
        ),
        // Profile screen
        BottomNavItem(
            tabName = "Favorites",
            tabIcon = Icons.Filled.Favorite,
            destination = Screens.Favorites
        ),
        BottomNavItem (
                tabName = "Cart",
        tabIcon = Icons.Filled.ShoppingCart,
        destination = Screens.Cart

    ))
    const val LISTING = "users/google/repos"
    const val PRODUCTS_LIST = "products"
    const val BASE_URL = "https://api.github.com/"
    const val PRODUCTS_BASE_URL = "https://dummyjson.com/"
    const val dummyDescription =
        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. "
}