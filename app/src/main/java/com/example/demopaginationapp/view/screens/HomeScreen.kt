package com.example.demopaginationapp.view.screens

import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.demopaginationapp.R
import com.example.demopaginationapp.model.dataclasses.Product
import com.example.demopaginationapp.model.networking.Resource
import com.example.demopaginationapp.model.networking.Status
import com.example.demopaginationapp.navigation.BottomNavItems
import com.example.demopaginationapp.navigation.Screens
import com.example.demopaginationapp.utils.BOLD_STYLE
import com.example.demopaginationapp.utils.CustomGlideImage
import com.example.demopaginationapp.utils.NORMAL_STYLE
import com.example.demopaginationapp.utils.RounderRecGlideImage
import com.example.demopaginationapp.utils.SMALL_BOLD_STYLE
import com.example.demopaginationapp.viewmodel.ProductViewModel
import com.google.accompanist.pager.HorizontalPagerIndicator
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(navController: NavHostController) {
    var backPressed : Boolean= false

    val context = LocalContext.current
    val activity = context as ComponentActivity
    val viewModel: ProductViewModel = hiltViewModel(viewModelStoreOwner = activity)
    val productsResource by viewModel.products.observeAsState(
        initial = Resource.loading(null)  //set loading state as initial -> display loader
    )
    val state = productsResource


    BackHandler(enabled = true) {
        if(backPressed){
            activity.finish()
        }
        else{
            backPressed = true
            Toast.makeText(context, "Press back again to exit", Toast.LENGTH_LONG).show()
        }
    }

    when (productsResource?.status) {
        Status.LOADING -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator() }
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
                modifier = Modifier.padding(16.dp))
        }
        else -> {}
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DisplayHome(data: List<Product>?, navController: NavHostController) {

    val images = ArrayList<String>()
    if (data != null) {
        for (d in data) if (images.size < 5) images.add(d.images[0])
    }
    val scrollState = rememberScrollState()
    val pageCount = images.size
    val pagerState = rememberPagerState(initialPage = 0) { pageCount }
    LaunchedEffect(pagerState) {
        while (true) {
            delay(2000) // Delay for 3 seconds
            val nextPage = (pagerState.currentPage + 1) % pageCount
            pagerState.animateScrollToPage(nextPage)
        }
    }


        Column(
            modifier = Modifier
                .padding(start = 15.dp, end = 15.dp, bottom = 15.dp)
                .verticalScroll(scrollState)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null  //set click ripple to null
                ) {
                    navController.navigate(Screens.ProductsList)
                }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier) {
                Text("Home", style = BOLD_STYLE, fontSize = 20.sp)
                Spacer(Modifier.weight(1f))
                IconButton(onClick = {
                    navController.navigate(Screens.Search)
                }) {
                    Icon(
                        imageVector = Icons.Filled.Search, // Standard back arrow icon
                        contentDescription = "Search",
                        tint = Color.Black,
                        modifier = Modifier.size(30.dp)
                    )
                }
                IconButton(onClick = {
                    navController.navigate(Screens.ReposList)
                }) {
                    Image(
                        painter = painterResource(R.drawable.outline_event_list_24), // Standard back arrow icon
                        contentDescription = "Repos",
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
            LazyRow(horizontalArrangement = Arrangement.spacedBy(15.dp)) {
                items(5) { topItem ->
                    Column(modifier = Modifier.width(105.dp)) {
                        CustomGlideImage(
                            data?.get(topItem)?.images[0].toString(),
                            0.dp,
                            3.dp,
                            4.dp,
                            CircleShape
                        )
                        Text(
                            text = data?.get(topItem)?.title ?: "", style = BOLD_STYLE,
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            maxLines = 1, fontSize = 14.sp
                        )
                    }
                }
            }
            Spacer(Modifier.height(15.dp))
            Text(text = "Today's Deals & Offers", style = BOLD_STYLE, fontSize = 18.sp)

            Box(
                modifier = Modifier
                    .height(230.dp)
                    .padding(vertical = 10.dp, horizontal = 6.dp)
                    .fillMaxWidth()
                    .shadow(
                        elevation = 4.dp, // Use a noticeable elevation for testing
                        shape = RoundedCornerShape(15.dp)
                    )
                    .background(Color.White, shape = RoundedCornerShape(15.dp))
                    .clip(RoundedCornerShape(15.dp))
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp),
                ) { page ->
                    RounderRecGlideImage(images[page])
                }
                HorizontalPagerIndicator(
                    pageCount = images.size,
                    pagerState = pagerState,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 10.dp)
                )
            }

            Spacer(Modifier.height(15.dp))
            Text(text = "Most Demanded Products", style = BOLD_STYLE, fontSize = 18.sp)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                modifier = Modifier.padding(6.dp)
            ) {
                items(data?.size ?: 0) { topItem ->
                    data?.get(topItem)?.let { GridItemCard(it, navController, 220.dp) }
                }
            }
            Spacer(Modifier.height(15.dp))
            Text(text = "Top Brands", style = BOLD_STYLE, fontSize = 18.sp)
            LazyHorizontalGrid(
                rows = GridCells.Fixed(2),
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 12.dp, horizontal = 6.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {

                items(data?.size ?: 0) { item ->
                    Log.d("kejfhgfwfew", "ProductScreen: ${data?.size ?: 0}")
                    data?.get(item)?.let {
                        CustomGlideImage(it.images[0], 0.dp, 3.dp, 4.dp, CircleShape)
                    }
                }
            }
            Spacer(Modifier.height(15.dp))
            Text(text = "Best Selling", style = BOLD_STYLE, fontSize = 18.sp)
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 6.dp)
            ) {

                val newList = data?.filter { it.brand != null }
                items(newList?.size ?: 0) { topItem ->
                    data?.get(topItem)?.let {
                        GridHorizontalItemCard(it, navController, 300.dp)
                    }
                }
            }
        }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun GridHorizontalItemCard(item: Product, navController: NavHostController, width: Dp, showFull : Boolean = false) {
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
            navController.navigate("product_detail_screen/${item.id}")
        },
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            GlideImage(
                model = item.images[0],
                contentDescription = "Product image",
                modifier = Modifier
                    .width(110.dp)
                    .height(110.dp),
            )
            Column(modifier = Modifier.weight(1f)) {
                if (showFull){
                    Text(
                        text = item.title,
                        style = BOLD_STYLE,
                        color = Color.Black,
                        maxLines = 1,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 5.dp)
                    )
                    Text(
                        text = item.brand?: "Dummy",
                        style = NORMAL_STYLE,
                        color = Color.Gray,
                        maxLines = 1,
                        fontSize = 15.sp,
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
                        }, fontSize = 15.sp,
                        modifier = Modifier.padding(top = 5.dp))
                }
                else{
                    Text(
                        text = item.brand?: "Dummy",
                        style = BOLD_STYLE,
                        color = Color.Black,
                        maxLines = 1,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(top = 5.dp),
                    )
                }
                Spacer(Modifier.height(5.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "rating icon",
                        tint = colorResource(R.color.golden),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(Modifier.width(3.dp))
                    Text(
                        text = String.format("%.1f", item.rating),
                        style = NORMAL_STYLE,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp, modifier = Modifier.padding(top = 5.dp))
                }
            }
            if (showFull){
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = "Fav icon",
                    modifier = Modifier
                        .clickable {
                            isFavorite = !isFavorite
                            item.isFav = !(item.isFav)
                        }
                        .padding(end = 5.dp))
            }
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavHostController) {

    NavigationBar(
        // set background color
        containerColor = Color.White,
        modifier = Modifier.drawBehind {
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color.Transparent, Color.Black),
                startY = -30f,
                endY = size.height
            ),
            topLeft = Offset(0f, -30f)
        )
    },) {
        // observe the backstack
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        // Bottom nav items
       BottomNavItems.forEach { navItem ->

            NavigationBarItem(
                // it currentRoute is equal then its selected route
                selected = currentRoute == navItem.destination,
                // navigate on click
                onClick = {
                    navController.navigate(navItem.destination)
                },
                // Icon of navItem
                icon = {
                    Icon(imageVector = navItem.tabIcon, contentDescription = navItem.tabName, modifier = Modifier.size(30.dp))
                },
                // label
                label = {
                    Text(text = navItem.tabName, style = SMALL_BOLD_STYLE)
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black, // Icon color when selected
                    unselectedIconColor = Color.Gray, // Icon color when not selected
                    selectedTextColor = Color.Black, // Label color when selected
                    indicatorColor = Color.White // Highlight color for selected item
                ),
            )
        }
    }
}


