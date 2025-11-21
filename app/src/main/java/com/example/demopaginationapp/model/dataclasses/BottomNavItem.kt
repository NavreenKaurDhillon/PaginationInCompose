package com.example.demopaginationapp.model.dataclasses

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem (

    val tabName: String,
    val tabIcon: ImageVector,
    val destination: String,
)