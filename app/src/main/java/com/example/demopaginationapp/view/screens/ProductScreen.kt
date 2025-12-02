package com.example.demopaginationapp.view.screens

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Chip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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


@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun ProductScreen(navController: NavHostController) {
    val context = LocalContext.current
    val activity = context as ComponentActivity
    val viewModel: ProductViewModel = hiltViewModel(viewModelStoreOwner = activity)
    val productsResource by viewModel.products.observeAsState(
        initial = Resource.loading(null)  //set loading state as initial -> display loader
    )
    val state = productsResource

    var activeSortOption by remember { mutableStateOf("Relevance") }
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        SortFilterDialog(
            activeSortOption,
            onDismiss = { showDialog = false },
            onSortSelected = { option ->
                activeSortOption = option
                viewModel.setSortOption(option)
                showDialog = false
            })
    }

        when (productsResource.status) {
            Status.LOADING -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator() }
            }
            Status.SUCCESS -> {
                val data = state.data?.products
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = {
                            // go back to last screen
                            navController.popBackStack()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Go back",
                                tint = Color.Black,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        Text(
                            text = "Products List",
                            style = BOLD_STYLE,
                            fontSize = 20.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )
                    }
                    data?.let {
                        ShowProductsList(
                            it,
                            navController,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    SortFilterBottomBar(
                        onFilterClick = { showDialog = true })
                }

            }
            Status.ERROR -> {
                // Show the error message
                Text(
                    text = "Failed to load products: ${state.message}",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp))
            }
            else -> {}
        }

}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ShowProductsList(
    data: List<Product>,
    navController: NavHostController,
    showFavIcon: Boolean = true,
    modifier: Modifier
) {

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(data.size) { item ->
                Log.d("kejfhgfwfew", "ProductScreen: ${data.size}")
                if (showFavIcon)
                    GridItemCard(data[item], navController, 0.dp, true)
                else
                    GridItemCard(data[item], navController, 0.dp, false)
            }
        }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GridItemCard(
    item: Product,
    navController: NavHostController,
    width: Dp,
    showFavIcon: Boolean = false
) {
    var isFavorite by remember { mutableStateOf(item.isFav) }
    ElevatedCard(
        modifier = if (width > 0.dp) {
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

        ), onClick = {
            navController.navigate("product_detail_screen/${item.id}")
        },
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(modifier = Modifier.height(160.dp)) {
                GlideImage(
                    model = item.images[0],
                    contentDescription = "Product image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                )
                if (showFavIcon) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Fav icon",
                        modifier = Modifier.clickable {
                            isFavorite = !isFavorite
                            item.isFav = !(item.isFav)
                        })
                }
            }
            Text(
                text = item.title,
                style = BOLD_STYLE,
                color = Color.Black,
                maxLines = 1,
                modifier = Modifier.padding(top = 5.dp)
            )
            Text(
                text = item.availabilityStatus,
                style = NORMAL_STYLE,
                color = Color.Gray,
                maxLines = 3,
                modifier = Modifier.padding(top = 5.dp)
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(BOLD_STYLE.toSpanStyle()) {
                        append("Price: ")
                    }
                    withStyle(NORMAL_STYLE.toSpanStyle()) {
                        append("$" + item.price.toString())
                    }
                },
                modifier = Modifier.padding(top = 5.dp)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "rating icon",
                    tint = colorResource(R.color.golden),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(3.dp))
                Text(
                    text = String.format("%.1f",item.rating),
                    style = NORMAL_STYLE,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp, modifier = Modifier.padding(top = 5.dp))
            }
        }
    }
}

@Composable
fun SortFilterBottomBar(
    onFilterClick: () -> Unit
) {
//    BottomAppBar(
//        modifier = Modifier
//            .fillMaxWidth(),
//        containerColor = Color.White,
//        tonalElevation = 8.dp,
//
//    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp)
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Button to open the dialog
            TextButton(onClick = onFilterClick, modifier = Modifier.weight(0.5f)) {
                Image(
                    painter = painterResource(R.drawable.sort_icon),
                    contentDescription = "Sort and Filter"
                )
                Spacer(Modifier.width(5.dp))
                Text("SORT", style = BOLD_STYLE)
            }
            Spacer(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 15.dp)
                    .width(1.dp) // thickness
                    .height(30.dp) //  height for the line
                    .background(Color.LightGray) //  color
            )
            TextButton(onClick = onFilterClick, modifier = Modifier.weight(0.5f)) {
                Image(
                    painter = painterResource(R.drawable.outline_filter_alt_24),
                    contentDescription = "Sort and Filter"
                )
                Spacer(Modifier.width(5.dp))
                Text("FILTER", style = BOLD_STYLE)
            }
        }
//    }
}

@Composable
fun SortFilterDialog(
    activeSortOption : String,
    onDismiss: () -> Unit,
    onSortSelected: (String) -> Unit,
) {
    // List of sorting options
    val sortOptions = listOf("Relevance", "Price Low to High", "Price High to Low", "Rating High to Low")
    var selectedOption by remember { mutableStateOf(activeSortOption) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Sort Options") },
        containerColor = Color.White,
        text = {
            Column {
                Text("Sort By:", fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(8.dp))
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    sortOptions.forEach { option ->
                        FilterChip(
                            selected = selectedOption == option,
                            onClick = {
                                selectedOption = option
                                      },
                            label = { Text(option) },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = colorResource(R.color.white),   // Unselected background color
                                selectedContainerColor = colorResource(R.color.black),  // Selected background color
                                labelColor = colorResource(R.color.black),   // Unselected text color
                                selectedLabelColor = colorResource(R.color.white)   //Selected text color
                            ))

                    }
                }
                // Separator
                Divider(Modifier.padding(vertical = 12.dp))
            }
        },
        confirmButton = {
              Button(onClick = {
                  onSortSelected(selectedOption)
              },  colors = ButtonDefaults.buttonColors(
                  containerColor = colorResource(R.color.green),
                  contentColor = Color.White,
              )) {
                  Text("APPLY")
              }
        },
        dismissButton = {

            TextButton(onClick = onDismiss) {
                Text("CANCEL")
            }
        }
    )
}