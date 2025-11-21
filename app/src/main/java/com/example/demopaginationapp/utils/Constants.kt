package com.example.demopaginationapp.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import com.example.demopaginationapp.model.dataclasses.BottomNavItem

object Constants {
    val BottomNavItems = listOf(
        BottomNavItem(
            tabName = "list_screen",
            tabIcon = Icons.Filled.Home,
            destination = "list_screen"
        ),

        BottomNavItem(
            tabName = "products_screen",
            tabIcon = Icons.Filled.Home,
            destination = "products"
        ),


        )
}