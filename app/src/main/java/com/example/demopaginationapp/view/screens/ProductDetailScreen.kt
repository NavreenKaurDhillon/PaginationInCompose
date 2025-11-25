package com.example.demopaginationapp.view.screens

import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.demopaginationapp.R
import com.example.demopaginationapp.model.dataclasses.Product
import com.example.demopaginationapp.model.dataclasses.Review
import com.example.demopaginationapp.utils.BOLD_STYLE
import com.example.demopaginationapp.utils.NORMAL_STYLE
import com.example.demopaginationapp.utils.RounderRecGlideImage
import com.example.demopaginationapp.utils.TopAppBar
import com.example.demopaginationapp.viewmodel.ProductViewModel
import com.google.gson.Gson
import kotlinx.coroutines.delay
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(data: String?, navController: NavController) {
    val context = LocalContext.current
    val activity = context as ComponentActivity
    val productViewModel: ProductViewModel = hiltViewModel(viewModelStoreOwner = activity)
    val decodedJsonString = remember(data) {
        if (data != null) {
            // Apply URLDecoder to convert '+' back to space ' '
            URLDecoder.decode(data, StandardCharsets.UTF_8.name())
        } else {
            null
        }
    }
    val responseData: Product = remember(decodedJsonString) {
        (if (decodedJsonString != null) {
            try {
                Gson().fromJson(decodedJsonString, Product::class.java)
            } catch (e: Exception) {
                Toast.makeText((context), "Exception caused is ${e.message}", Toast.LENGTH_LONG)
                    .show()
                Log.d("hhhh", "DetailScreen: exeption caused is ${e.message} ")
            }

        } else
            null) as Product
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar("Details", true, navController)
        }) { paddingValues ->
        ShowDetails(responseData, paddingValues, navController, productViewModel)
    }

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ShowDetails(
    responseData: Product,
    paddingValues: PaddingValues,
    navController: NavController,
    productViewModel: ProductViewModel
) {
    val pageCount = responseData.images.size
    val scrollState = rememberScrollState()
    val addedToCart = productViewModel.cartProducts.contains(responseData)
    val pagerState = rememberPagerState(initialPage = 0) { pageCount }
    var descriptionExpanded by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(pagerState) {
        // Coroutine loop for auto-scrolling
        while (true) {
            delay(1500) // Delay for 3 seconds
            val nextPage = (pagerState.currentPage + 1) % pageCount
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 20.dp)
            .verticalScroll(scrollState)
    ) {
        Spacer(Modifier.height(5.dp))
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp) // Set a fixed height for the banner
        ) { page ->
            RounderRecGlideImage(responseData.images[page])
        }
        Spacer(Modifier.height(20.dp))
        Text(
            color = Color.Black,
            text = buildAnnotatedString {
                withStyle(BOLD_STYLE.toSpanStyle()) {
                    append("Name :")
                }
                withStyle(NORMAL_STYLE.toSpanStyle()) {
                    append(responseData.title)
                }
            }
        )
        Text(
            color = Color.Black,
            text = buildAnnotatedString {
                withStyle(BOLD_STYLE.toSpanStyle()) {
                    append("Price :")
                }
                withStyle(NORMAL_STYLE.toSpanStyle()) {
                    append(responseData.price.toString())
                }
            }
        )
        Text(
            color = Color.Black,
            text = buildAnnotatedString {
                withStyle(BOLD_STYLE.toSpanStyle()) {
                    append("Brand :")
                }
                withStyle(NORMAL_STYLE.toSpanStyle()) {
                    append(responseData.brand)
                }
            }
        )
        Text(
            color = Color.Black,
            text = buildAnnotatedString {
                withStyle(BOLD_STYLE.toSpanStyle()) {
                    append("Warranty :")
                }
                withStyle(NORMAL_STYLE.toSpanStyle()) {
                    append(responseData.warrantyInformation)
                }
            }
        )
        Text(
            color = Color.Black,
            text = buildAnnotatedString {
                withStyle(BOLD_STYLE.toSpanStyle()) {
                    append("Description :")
                }
                withStyle(NORMAL_STYLE.toSpanStyle()) {
                    append(responseData.description)
                }
            },
            maxLines = if (descriptionExpanded) Int.MAX_VALUE else 3,  //max 3 lines for description
            overflow = TextOverflow.Ellipsis //show ... if text is more than 3 lines
        )
        TextButton(
            onClick = {
                descriptionExpanded = !descriptionExpanded
            },
        ) {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = if (descriptionExpanded) painterResource(R.drawable.baseline_keyboard_arrow_up_24) else painterResource(
                        R.drawable.baseline_keyboard_arrow_down_24
                    ),
                    tint = Color.Blue,
                    contentDescription = "expand text",
                    modifier = Modifier
                        .size(25.dp)
                        .padding(end = 5.dp)
                )
                Text(
                    text = if (descriptionExpanded) "Show Less" else "Show More",
                    fontSize = 14.sp,
                    style = NORMAL_STYLE,
                    color = Color.Blue,
                    textDecoration = TextDecoration.Underline
                )
            }
        }
        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(responseData?.reviews?.size ?: 0) { topItem ->
                responseData?.reviews?.get(topItem)?.let { ReviewItemGrid(it) }
            }
        }
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = {
                if (!addedToCart)
                    productViewModel.cartProducts.add(responseData)
                Log.d("ererhhre", "CartScreen:  aaddedd ${productViewModel.cartProducts}")
                if (addedToCart)
                    navController.navigate("cart_screen")
            }, modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black, // Sets the background color of the button
                contentColor = Color.White    // Sets the color of the text/icon inside the button
            )
        ) {
            Icon(
                painter = if (addedToCart) painterResource(R.drawable.cart_icon) else painterResource(
                    R.drawable.add_to_cart_icon
                ),
                contentDescription = "cart"
            )
            Text(
                text =
                    if (addedToCart) "GO TO CART" else "ADD TO CART",
                textAlign = TextAlign.Center,
                style = BOLD_STYLE,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 15.dp, vertical = 6.dp)
            )
        }
        ElevatedButton(
            onClick = {
                navController.navigate("product_screen")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White, // Sets the background color of the button
                contentColor = Color.Black    // Sets the color of the text/icon inside the button
            ),
        ) {
            Text(text = "SHOW SIMILAR", textAlign = TextAlign.Center, style = BOLD_STYLE)

        }

    }
}


@Composable
fun ReviewItemGrid(review: Review) {
    ElevatedCard(
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 5.dp)
            .width(200.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = 5.dp
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            Text(
                text = review.reviewerName,
                style = BOLD_STYLE,
                color = Color.Black,
                maxLines = 1,
                modifier = Modifier.padding(top = 5.dp)
            )
            Text(
                text = review.comment ?: "",
                style = NORMAL_STYLE,
                color = Color.Gray,
                maxLines = 3,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 5.dp)
            )
            Text(
                text = "⭐ ${review.rating.toString()}",
                style = NORMAL_STYLE,
                fontSize = 14.sp, modifier = Modifier.padding(top = 5.dp)
            )
        }
    }
}