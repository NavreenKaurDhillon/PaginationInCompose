package com.example.demopaginationapp.view.screens

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.demopaginationapp.model.dataclasses.Product
import com.example.demopaginationapp.model.dataclasses.ResponseDataItem
import com.google.gson.Gson
import kotlinx.coroutines.delay

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ProductDetailScreen(data: String?, navController: NavController) {
    val context = LocalContext.current
    val responseData : Product = remember(data) {
        (if (data!=null){
            try {
                Gson().fromJson(data, Product::class.java)
            } catch (e: Exception){
                Toast.makeText((context), "Exception caused is ${e.message}", Toast.LENGTH_LONG).show()
                Log.d("hhhh", "DetailScreen: exeption caused is ${e.message} ")
            }
        } else
            null) as Product
    }

    val pageCount = responseData.images.size
    val pagerState = rememberPagerState(initialPage = 0) { pageCount }

    Log.d("kjfkjbrf", "ProductDetailScreen: $pageCount")
    // --- 1. Auto-scrolling Effect ---
    LaunchedEffect(pagerState) {
        // Coroutine loop for auto-scrolling
        while (true) {
            delay(3000) // Delay for 3 seconds
            val nextPage = (pagerState.currentPage + 1) % pageCount
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp),
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp) // Set a fixed height for the banner
        ) { page ->
            GlideImage(
                model = responseData.images[page],
                contentDescription = "Logo description of the repo",
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        Text(
            color = Color.Black,
            text = buildAnnotatedString {
                withStyle(BOLD_STYLE.toSpanStyle()){
                    append("Name :")
                }
                withStyle(NORMAL_STYLE.toSpanStyle()){
                    append(responseData.title)
                }
            }
        )
        Text(
            color = Color.Black,
            text = buildAnnotatedString {
                withStyle(BOLD_STYLE.toSpanStyle()){
                    append("Price :")
                }
                withStyle(NORMAL_STYLE.toSpanStyle()){
                    append(responseData.price.toString())
                }
            }
        )
   Text(
            color = Color.Black,
            text = buildAnnotatedString {
                withStyle(BOLD_STYLE.toSpanStyle()){
                    append("Brand :")
                }
                withStyle(NORMAL_STYLE.toSpanStyle()){
                    append(responseData.brand)
                }
            }
        )
        Text(
            color = Color.Black,
            text = buildAnnotatedString {
                withStyle(BOLD_STYLE.toSpanStyle()){
                    append("Warranty :")
                }
                withStyle(NORMAL_STYLE.toSpanStyle()){
                    append(responseData.warrantyInformation)
                }
            }
        )
        Text(
            color = Color.Black,
            text = buildAnnotatedString {
                withStyle(BOLD_STYLE.toSpanStyle()){
                    append("Description :")
                }
                withStyle(NORMAL_STYLE.toSpanStyle()){
                    append(responseData.description)
                }
            }
        )
        Spacer(Modifier.height(20.dp))
        Button(onClick = {
            navController.navigate("product_screen")
        },modifier = Modifier.padding(8.dp).align(Alignment.CenterHorizontally).fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black, // Sets the background color of the button
                contentColor = Color.White    // Sets the color of the text/icon inside the button
            )) {
            Text(text = "ADD TO CART",  textAlign = TextAlign.Center, style = BOLD_STYLE, modifier = Modifier.padding(horizontal = 15.dp, vertical = 6.dp))

        }
//        Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
            ElevatedButton (onClick = {
                navController.navigate("list_screen")
            },modifier = Modifier.fillMaxWidth().padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White, // Sets the background color of the button
                    contentColor = Color.Black    // Sets the color of the text/icon inside the button
                ), ) {
                Text(text = "SHOW SIMILAR",  textAlign = TextAlign.Center, style = BOLD_STYLE)

            }
           /* Spacer(Modifier.width(30.dp))
            Button(onClick = {
                navController.navigate("list_screen")
            },modifier = Modifier.weight(0.5f).padding(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black, // Sets the background color of the button
                    contentColor = Color.White    // Sets the color of the text/icon inside the button
                )) {
                Text(text = "HOME",  textAlign = TextAlign.Center, style = BOLD_STYLE)

            }*/
//        }

    }
}