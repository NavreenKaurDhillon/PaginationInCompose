package com.example.demopaginationapp.view.screens

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.demopaginationapp.model.dataclasses.Product
import com.example.demopaginationapp.navigation.Screens
import com.example.demopaginationapp.utils.BOLD_STYLE
import com.example.demopaginationapp.utils.NORMAL_STYLE
import com.example.demopaginationapp.utils.TopAppBar
import com.example.demopaginationapp.viewmodel.ProductViewModel



@Composable
fun FavScreen(navController: NavHostController) {
    val context = LocalContext.current
    val activity = context as ComponentActivity
    val viewModel: ProductViewModel = hiltViewModel(viewModelStoreOwner = activity)
    val favoritesList = ArrayList<Product>()
    viewModel.products.value?.data?.products?.forEach { if(it.isFav) favoritesList.add(it)  }

    BackHandler(enabled = true) {
        //Handle the back press manually -> navigate to home
        navController.navigate(Screens.Home) {
            popUpTo(Screens.Home) {
                inclusive = true  // remove other entries and goto home
            }
            launchSingleTop = true
        }
    }
    Scaffold(topBar = { TopAppBar("Favorites", false, navController) }, containerColor = Color.White) { paddingValues ->
        if (favoritesList.size>0) {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(12.dp),
            ) {
                items(favoritesList?.size ?: 0) { item ->
                    favoritesList?.let {
                        FavItemCard(
                            it[item],
                            navController,
                            0.dp,
                            true,
                        )
                    }
                }
            }
        }
        else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "No products found!", style = NORMAL_STYLE, textAlign = TextAlign.Center)
            }
        }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FavItemCard(item: Product, navController: NavHostController, width: Dp, showFull : Boolean = false) {
    var isFavorite by remember { mutableStateOf(item.isFav) }
    ElevatedCard(
        modifier = if(width>0.dp){
            Modifier
                .padding(vertical = 10.dp, horizontal = 5.dp)
                .width(width) }else
        {
            Modifier
                .padding(vertical = 10.dp, horizontal = 5.dp)
                .fillMaxWidth()
        },
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White
        ), onClick = {
            navController.navigate("product_detail_screen/${item.id.toString()}")
        },
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 5.dp)
    ) {
        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            GlideImage(
                model = item.images[0],
                contentDescription = "Product image",
                modifier = Modifier
                    .width(110.dp)
                    .height(110.dp),)
            Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.title ?: "",
                        style = BOLD_STYLE,
                        color = Color.Black,
                        maxLines = 1,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 5.dp))
                    Text(
                        text = item.brand ?: "",
                        style = NORMAL_STYLE,
                        color = Color.Gray,
                        maxLines = 1,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(top = 5.dp))
                    Text(
                        text = buildAnnotatedString {
                            withStyle(BOLD_STYLE.toSpanStyle()){
                                append("Price: ")
                            }
                            withStyle(NORMAL_STYLE.toSpanStyle()){
                                append("$"+item.price.toString())
                            }
                        }, fontSize = 15.sp,
                        modifier = Modifier.padding(top = 5.dp))
                Text(
                    text = "⭐ ${String.format("%.1f", item.rating)}",
                    modifier = Modifier.padding(top = 5.dp),
                    style = NORMAL_STYLE,
                    fontSize = 14.sp
                )
            }
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Fav icon",
                    modifier = Modifier.clickable {
                        isFavorite = !isFavorite
                        item.isFav = !(item.isFav)

                    }.padding(end = 5.dp))
        }
    }
}