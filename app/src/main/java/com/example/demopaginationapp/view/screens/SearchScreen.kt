package com.example.demopaginationapp.view.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.placeholder
import com.example.demopaginationapp.utils.BOLD_STYLE
import com.example.demopaginationapp.utils.NORMAL_STYLE
import com.example.demopaginationapp.viewmodel.ProductViewModel

@Composable
fun SearchScreen(navController: NavHostController) {
    var searchText by remember { mutableStateOf("") }
    val context = LocalContext.current
    val activity = context as ComponentActivity
    val viewModel: ProductViewModel = hiltViewModel(viewModelStoreOwner = activity)

    val filteredProducts by remember(searchText, viewModel.products.value?.data?.products) {
        if (searchText.isBlank()) {
            mutableStateOf(viewModel.products.value?.data?.products)
        } else {
            mutableStateOf(
                viewModel.products.value?.data?.products?.filter { product ->
                    product.title.contains(searchText, ignoreCase = true) ||
                            product.brand?.contains(searchText, ignoreCase = true) == true
                }
            )
        }
    }

    Scaffold(containerColor = Color.White) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            TextField(
                value = searchText,
                onValueChange = { searchText = it},
                placeholder = { Text("Search...", style = NORMAL_STYLE, color = Color.Gray) },
                leadingIcon = {
                    IconButton(onClick = {
                        // go back to last screen
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Standard back arrow icon
                            contentDescription = "Go back",
                            tint = Color.Black,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    // 1. APPLY THE ROUNDED BACKGROUND TO THE ENTIRE TEXT FIELD AREA
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(16.dp),
                    )
                    .padding(horizontal = 8.dp),
                colors = TextFieldDefaults.colors(
                    // Set the container color to transparent, as we are using the Modifier background
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,

                    // Remove the indicator line below the TextField
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                trailingIcon = {
                    // Clear text button
                    if (searchText.isNotEmpty()) {
                        IconButton(onClick = { searchText = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear Search")
                        }
                    }
                },
                singleLine = true
        )
            if (filteredProducts.isNullOrEmpty())
            {
                Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center){
                    Text(text="No products found!", style = NORMAL_STYLE, textAlign = TextAlign.Center)
                }
            }
            else{
                filteredProducts?.let { ShowProductsList(PaddingValues(horizontal = 5.dp, vertical = 5.dp), it, navController, false) }
            }
        }
    }
}