package com.example.demopaginationapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import com.example.demopaginationapp.model.dataclasses.BottomNavItem

val BottomNavItems = listOf(
    // Home screen
    BottomNavItem(
        tabName = "Home",
        tabIcon = Icons.Filled.Home,
        destination = Screens.Home
    ),
    // Categories list
    BottomNavItem(
        tabName = "Categories",
        tabIcon =Icons.Filled.Person,
        destination = Screens.Categories
    ),
    // Products list
    BottomNavItem(
        tabName = "Brands",
        tabIcon = Icons.Filled.List,
        destination = Screens.Brands
    ),
    // Favorites
    BottomNavItem(
        tabName = "Favorites",
        tabIcon = Icons.Filled.Favorite,
        destination = Screens.Favorites
    ),
    //cart
    BottomNavItem (
        tabName = "Cart",
        tabIcon = Icons.Filled.ShoppingCart,
        destination = Screens.Cart

    ))