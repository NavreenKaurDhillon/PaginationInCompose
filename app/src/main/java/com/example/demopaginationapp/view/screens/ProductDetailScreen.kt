package com.example.demopaginationapp.view.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.demopaginationapp.R
import com.example.demopaginationapp.model.dataclasses.Product
import com.example.demopaginationapp.model.dataclasses.Review
import com.example.demopaginationapp.navigation.Screens
import com.example.demopaginationapp.utils.BOLD_STYLE
import com.example.demopaginationapp.utils.NORMAL_STYLE
import com.example.demopaginationapp.utils.RounderRecGlideImage
import com.example.demopaginationapp.utils.TopAppBar
import com.example.demopaginationapp.viewmodel.ProductViewModel
import com.google.accompanist.pager.HorizontalPagerIndicator
import kotlinx.coroutines.delay

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(data: String?, navController: NavController) {
    val context = LocalContext.current
    val activity = context as ComponentActivity
    val productViewModel: ProductViewModel = hiltViewModel(viewModelStoreOwner = activity)

    var responseData: Product? = null
    for (p in productViewModel.products.value?.data?.products!!)
        if (p.id == data?.toInt())
            responseData = p


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
                text = "Details",
                style = BOLD_STYLE,
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
        }
        responseData?.let { ShowDetails(it, navController, productViewModel) }
    }


}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ShowDetails(
    responseData: Product,
    navController: NavController,
    productViewModel: ProductViewModel
) {
    val pageCount = responseData.images.size
    val scrollState = rememberScrollState()
    val addedToCart = productViewModel.cartProducts.contains(responseData)
    val pagerState = rememberPagerState(initialPage = 0) { pageCount }
    var descriptionExpanded by remember { mutableStateOf(false) }
    var textOverflows by remember { mutableStateOf(false) }
    LaunchedEffect(pagerState) {
        // Coroutine loop for auto-scrolling
        while (true) {
            delay(2000) // Delay for 3 seconds
            val nextPage = (pagerState.currentPage + 1) % pageCount
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 15.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Spacer(Modifier.height(5.dp))
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
        )
        {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp) // Set a fixed height for the banner
            ) { page ->
                RounderRecGlideImage(responseData.images[page])
            }
            if (responseData.images.size > 1) {
                HorizontalPagerIndicator(
                    pageCount = responseData.images.size,
                    pagerState = pagerState,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 10.dp)
                )
            }

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
                    append("$" + responseData.price.toString())
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
                    append(responseData.brand ?: "Dummy")
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
            onTextLayout = { textLayoutResult ->
                // This state is updated only when the text is in its collapsed state
                if (!descriptionExpanded) {
                    textOverflows = textLayoutResult.hasVisualOverflow
                }
            },
            maxLines = if (descriptionExpanded) Int.MAX_VALUE else 3,  //max 3 lines for description
            overflow = TextOverflow.Ellipsis //show ... if text is more than 3 lines
        )

        if (textOverflows) {

            TextButton(
                onClick = {
                    descriptionExpanded = !descriptionExpanded
                }, colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                )
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
        }
        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(responseData.reviews.size) { topItem ->
                ReviewItemGrid(responseData.reviews[topItem])
            }
        }
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = {
                if (!addedToCart)
                    productViewModel.cartProducts.add(responseData)
                if (addedToCart)
                    navController.navigate(Screens.Cart)
            }, modifier = Modifier
                .padding(8.dp)
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(), colors = ButtonDefaults.buttonColors(
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
                navController.navigate(Screens.ProductsList)
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
            defaultElevation = 4.dp
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            Text(
                text = review.reviewerName,
                style = BOLD_STYLE,
                color = Color.Black,
                maxLines = 1,
                modifier = Modifier.padding(top = 5.dp),
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = review.comment,
                style = NORMAL_STYLE,
                color = Color.Gray,
                maxLines = 1,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 5.dp),
                overflow = TextOverflow.Ellipsis
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
                    text = String.format("%.1f", review.rating.toDouble()),
                    style = NORMAL_STYLE,
                    textAlign = TextAlign.Center,
                    fontSize = 14.sp, modifier = Modifier.padding(top = 5.dp)
                )
            }
        }
    }
}