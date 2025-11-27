package com.example.demopaginationapp.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.demopaginationapp.utils.BOLD_STYLE
import com.example.demopaginationapp.utils.TopAppBar

@Composable
fun CouponScreen(navController: NavHostController) {
    Scaffold(
        topBar = { TopAppBar("Apply Coupons", true, navController)},
        containerColor = Color.White)
    { paddingValues ->
        Column (
            modifier = Modifier.padding(paddingValues).padding(10.dp)) {
     Text(text = "No coupons found!", style = BOLD_STYLE, fontSize = 20.sp, modifier = Modifier.fillMaxWidth().padding(vertical = 100.dp), textAlign = TextAlign.Center)
        }
    }
}