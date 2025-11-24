package com.example.demopaginationapp.view.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.demopaginationapp.R
import com.example.demopaginationapp.model.dataclasses.Product
import com.example.demopaginationapp.model.networking.Resource
import com.example.demopaginationapp.model.networking.Status
import com.example.demopaginationapp.utils.BOLD_STYLE
import com.example.demopaginationapp.utils.NORMAL_STYLE
import com.example.demopaginationapp.viewmodel.ProductViewModel
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets



@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun ProductScreen( navController: NavHostController) {

    val viewModel : ProductViewModel = hiltViewModel()
    val productsResource by viewModel.products.observeAsState()

    var showDialog by remember { mutableStateOf(false) }

    // Handle the initial null state before the first emission, or a null-emitting error
    val state = productsResource ?: Resource.loading(null)

    // 2. Handle the different states (Loading, Success, Error)
    when (productsResource?.status) {
        Status.LOADING -> {
            // Show a full-screen loading indicator
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        Status.SUCCESS -> {
            // Data loaded successfully
            val data = state.data?.products // Safely access the data

            if (data?.isNotEmpty() == true) {
                Log.d("ekkhejfkjewjfk", "ProductScreen: ounn   $showDialog")
                if(showDialog){
                    SortFilterDialog(
                        currentSortOption = "None", // Replace with state from ViewModel
                        onDismiss = { showDialog = false },
                        onSortSelected = { option ->
                            viewModel.setSortOption(option) // Implement this
                            showDialog = false
                        },
                        onFilterApplied = { brand, maxPrice ->
                            // viewModel.applyFilters(brand, maxPrice) // Implement this
                            showDialog = false
                        },
                        onFilterClear = {
                            // viewModel.clearFilters() // Implement this
                            showDialog = false
                        }
                    )
                }

                Scaffold(
                    topBar = {
                        com.example.demopaginationapp.utils.TopAppBar(
                            "Products List",
                            true,
                            navController
                        )
                    },
                    bottomBar = {
                        SortFilterBottomBar(
                            onFilterClick = { showDialog = true }
                        )
                    },
                    containerColor = Color.White,
                ) { paddingValues ->
                    ShowProductsList(paddingValues, data, navController)
                }
            } else {
                Text("No products available.", modifier = Modifier.padding(16.dp))
            }
        }
        Status.ERROR -> {
            // Show the error message
            Text(
                text = "Failed to load products: ${state.message}",
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }

        else -> {}
    }


}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ShowProductsList(
    paddingValues: PaddingValues,
    data: List<Product>,
    navController: NavHostController
) {

        LazyVerticalGrid(
            // 2. Define the columns: GridCells.Fixed(2) creates exactly two columns
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth(),
            // 3. Add padding around the entire grid content
            contentPadding = PaddingValues(12.dp),

            // 4. Set the spacing between rows and columns
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            // 5. Populate the grid using the items extension function
            items(data.size) { item ->
                Log.d("kejfhgfwfew", "ProductScreen: ${data.size}")
                GridItemCard(data[item], navController, 0.dp)
            }
        }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GridItemCard(item: Product, navController: NavHostController, width : Dp) {

    ElevatedCard(
        modifier = if(width>0.dp){
            Modifier
                .padding(vertical = 10.dp, horizontal = 5.dp)
                .width(width)
        } else {
            Modifier
                .padding(vertical = 10.dp, horizontal = 5.dp)
                .fillMaxWidth()
        },
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White

        )  , onClick = {

            //send response object
            val jsonString = Gson().toJson(item)
            val encodedJson = URLEncoder.encode(jsonString, StandardCharsets.UTF_8.name())

            navController.navigate("product_detail_screen/$encodedJson")
        },
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 12.dp
        ))  {

        Column(modifier = Modifier.padding(12.dp)) {

            GlideImage(
                model = item.images[0],
                contentDescription = "Product image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),

                )
            Text(
                text = item.title,
                style = BOLD_STYLE,
                color = Color.Black,
                maxLines = 1,
                modifier = Modifier.padding(top = 5.dp)
            )
            Text(
                text = item.availabilityStatus?:"",
                style = NORMAL_STYLE,
                color = Color.Gray,
                maxLines = 3,
                modifier = Modifier.padding(top = 5.dp)
            )


            Text(
                text = buildAnnotatedString {
                    withStyle(BOLD_STYLE.toSpanStyle()){
                        append("Price: ")
                    }
                    withStyle(NORMAL_STYLE.toSpanStyle()){
                        append("$"+item.price.toString())
                    }
                },
                modifier = Modifier.padding(top = 5.dp)
            )

            Log.d("ekkhejfkjewjfk", "GridItemCard:${item.rating.toString()} ")
                Text(
                    //error

                    text = "⭐ ${item.rating.toString()}",
                    style = NORMAL_STYLE,
                    fontSize = 14.sp
                )
        }
    }
}


@Composable
fun SortFilterBottomBar(
    onFilterClick: () -> Unit
) {
    BottomAppBar(
        modifier = Modifier
            .fillMaxWidth(),
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Button to open the dialog
            TextButton(onClick = onFilterClick, modifier = Modifier.weight(0.5f)) {
                Image(
                    painter = painterResource(R.drawable.sort_icon),
                    contentDescription = "Sort and Filter")
                Spacer(Modifier.width(5.dp))
                Text("SORT", style = BOLD_STYLE)
            }
            Spacer(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 15.dp)
                    .width(1.dp) // This sets the thickness
                    .height(30.dp) // Explicit height for the line
                    .background(Color.LightGray) // This sets the color
            )
            TextButton(onClick = onFilterClick, modifier = Modifier.weight(0.5f)) {
                Image(
                    painter = painterResource(R.drawable.outline_filter_alt_24),
                    contentDescription = "Sort and Filter")
                Spacer(Modifier.width(5.dp))
                Text("FILTER", style = BOLD_STYLE)
            }
        }
    }
}


@Composable
fun SortFilterDialog(
    currentSortOption: String,
    onDismiss: () -> Unit,
    onSortSelected: (String) -> Unit,
    onFilterApplied: (brand: String?, maxPrice: Double?) -> Unit,
    onFilterClear: () -> Unit
) {
    // Local states for filter inputs
    val availableBrands = remember { listOf("None", "Sony", "Logitech", "Dell", "Razer", "Anker", "Apple", "Samsung") }
    var selectedBrand by remember { mutableStateOf("None") }
    var maxPriceText by remember { mutableStateOf("") }

    // List of sorting options
    val sortOptions = listOf("None", "Price Low to High", "Price High to Low", "Rating High to Low")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Sort Options") },
        containerColor = Color.White,
        text = {
            Column {

                // --- SORTING OPTIONS ---
                Text("Sort By:", fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    sortOptions.forEach { option ->
                        FilterChip(
                            selected = currentSortOption == option,
                            onClick = { onSortSelected(option) },
                            label = { Text(option) },

                        )
                    }
                }

                // Separator
                Divider(Modifier.padding(vertical = 12.dp))

                /* // --- FILTER BY BRAND ---
                 Text("Filter By Brand:", fontWeight = FontWeight.SemiBold)
                 Spacer(Modifier.height(8.dp))
                 // Dropdown or another selection method for brand
                 OutlinedTextField(
                     value = selectedBrand,
                     onValueChange = { selectedBrand = it },
                     label = { Text("Brand") },
                     readOnly = true,
                     trailingIcon = { *//* IconButton for dropdown *//* }
                    // A proper dropdown (ExposedDropdownMenuBox) is complex but better
                )*/

                /*  // --- FILTER BY MAX PRICE ---
                  Spacer(Modifier.height(8.dp))
                  Text("Filter By Max Price:", fontWeight = FontWeight.SemiBold)
                  OutlinedTextField(
                      value = maxPriceText,
                      onValueChange = { newValue ->
                          // Only allow numerical input
                          maxPriceText = newValue.filter { it.isDigit() || it == '.' }
                      },
                      label = { Text("Max Price (€/$)") },
                      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                  )*/
            }
        },
        confirmButton = {
            Button(onClick = {
                val maxPrice = maxPriceText.toDoubleOrNull()
                onFilterApplied(selectedBrand, maxPrice)
            }) {
                Text("APPLY FILTERS")
            }
        },
        dismissButton = {

            TextButton(onClick = onDismiss) {
                Text("CANCEL")
            }
        }
    )
}