package com.example.demopaginationapp.view.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
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
import com.example.demopaginationapp.model.networking.Resource
import com.example.demopaginationapp.model.networking.Status
import com.example.demopaginationapp.utils.BOLD_STYLE
import com.example.demopaginationapp.utils.CustomGlideImage
import com.example.demopaginationapp.utils.NORMAL_STYLE
import com.example.demopaginationapp.utils.RounderRecGlideImage
import com.example.demopaginationapp.utils.TopAppBar
import com.example.demopaginationapp.viewmodel.ProductViewModel
import com.google.gson.Gson
import kotlinx.coroutines.delay
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun HomeScreen(navController: NavHostController) {
    val viewModel : ProductViewModel = hiltViewModel()
    val productsResource by viewModel.products.observeAsState()
    val state = productsResource ?: Resource.loading(null)

    when (productsResource?.status) {
        Status.LOADING -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        Status.SUCCESS -> {
            val data = state.data?.products
            DisplayHome(data, navController)

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
fun DisplayHome(data: List<Product>?, navController: NavHostController) {

    val images = ArrayList<String>()
    if (data != null) {
        for(d in data) images.add(d.images[0])
    }
    val scrollState = rememberScrollState()
    val pageCount = images.size
    val pagerState = rememberPagerState(initialPage = 0) { pageCount }
    LaunchedEffect(pagerState) {
        while (true) {
            delay(1500) // Delay for 3 seconds
            val nextPage = (pagerState.currentPage + 1) % pageCount
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Scaffold(
        topBar = { TopAppBar("Home", false, navController = navController) },
        containerColor = Color.White,
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues).padding( horizontal = 16.dp)
            .verticalScroll(scrollState)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null  //set click ripple to null
            ) {
                navController.navigate("product_screen")
            }) {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(15.dp),) {
                items(5) {topItem->
                    Column(modifier = Modifier.width(105.dp)) {
                        CustomGlideImage(data?.get(topItem)?.images[0].toString(), 100.dp, 3.dp, 4.dp , CircleShape)
                        Text(text = data?.get(topItem)?.title?:"", style = BOLD_STYLE ,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp).fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            maxLines = 1, fontSize = 14.sp)
                    }
                }
            }
            Spacer(Modifier.height(15.dp))
            Text(text = "Today's Deals & Offers", style = BOLD_STYLE, fontSize = 18.sp)
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .padding(vertical = 10.dp)// Set a fixed height for the banner
            ) { page ->
                RounderRecGlideImage(images[page])
            }
            Spacer(Modifier.height(15.dp))
            Text(text = "Most Demanded Products", style = BOLD_STYLE, fontSize = 18.sp)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(15.dp),
                modifier = Modifier.padding(12.dp)) {
                items(data?.size?:0) {topItem->
                    data?.get(topItem)?.let { GridItemCard(it , navController, 220.dp) }
                }
            }
            Spacer(Modifier.height(15.dp))
            Text(text = "Top Brands", style = BOLD_STYLE, fontSize = 18.sp)
            LazyHorizontalGrid(
                // 2. Define the columns: GridCells.Fixed(2) creates exactly two columns
                rows = GridCells.Fixed(2),
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                // 3. Add padding around the entire grid content
                contentPadding = PaddingValues(12.dp),

                // 4. Set the spacing between rows and columns
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                // 5. Populate the grid using the items extension function
                items(data?.size?:0) { item ->
                    Log.d("kejfhgfwfew", "ProductScreen: ${data?.size?:0}")
                    data?.get(item)?.let { CustomGlideImage(it?.images[0].toString(), 100.dp, 3.dp, 4.dp , CircleShape)
                    }
                }
            }
            Spacer(Modifier.height(15.dp))
            Text(text = "Best Selling", style = BOLD_STYLE, fontSize = 18.sp)
            LazyRow(horizontalArrangement = Arrangement.spacedBy(15.dp),
                modifier = Modifier.padding(12.dp)) {
                items(data?.size?:0) {topItem->
                    data?.get(topItem)?.let {
                        GridHorizontalItemCard(it , navController, 300.dp) }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GridHorizontalItemCard(item: Product, navController: NavHostController, width : Dp) {
    ElevatedCard(
        modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 5.dp)
                .width(width),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White
        )  , onClick = {
            //send response object
            val jsonString = Gson().toJson(item)
            val encodedJson = URLEncoder.encode(jsonString, StandardCharsets.UTF_8.name())
            navController.navigate("product_detail_screen/$encodedJson")
        },
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 5.dp
        ))  {
        Row (modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            GlideImage(
                model = item.images[0],
                contentDescription = "Product image",
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp),)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.brand?:"",
                    style = BOLD_STYLE,
                    color = Color.Black,
                    maxLines = 1,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 5.dp)
                )
              Spacer(Modifier.height(10.dp))
                Text(
                    //error
                    text = "⭐ ${item.rating.toString()}",
                    style = NORMAL_STYLE,
                    fontSize = 14.sp
                )
            }
        }
    }
}

