package com.example.demopaginationapp.view.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.demopaginationapp.model.dataclasses.Product
import com.example.demopaginationapp.model.networking.Resource
import com.example.demopaginationapp.model.networking.Status
import com.example.demopaginationapp.viewmodel.ProductViewModel
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets



@Composable
fun ProductScreen( navController: NavHostController) {

    val viewModel : ProductViewModel = hiltViewModel()
    val productsResource by viewModel.products.observeAsState()

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
                Log.d("ekkhejfkjewjfk", "ProductScreen: ounn")
                LazyVerticalGrid(
                    // 2. Define the columns: GridCells.Fixed(2) creates exactly two columns
                    columns = GridCells.Fixed(2),

                    // 3. Add padding around the entire grid content
                    contentPadding = PaddingValues(16.dp),

                    // 4. Set the spacing between rows and columns
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),

                    modifier = Modifier.fillMaxSize(),
                ) {
                    // 5. Populate the grid using the items extension function
                    items(data.size) { item ->
                        GridItemCard(data.get(item), navController)
                    }
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
fun GridItemCard(item: Product, navController: NavHostController) {

    ElevatedCard( modifier = Modifier
        .padding(vertical = 10.dp, horizontal = 5.dp)
        .fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White

        )  , onClick = {

            //send response object
            val jsonString = Gson().toJson(item)
            val encodedJson = URLEncoder.encode(jsonString, StandardCharsets.UTF_8.name())
            navController.navigate("product_detail_screen/$encodedJson")
        },)  {

        Column(modifier = Modifier.padding(12.dp)) {

            GlideImage(
                model = item.images.get(0),
                contentDescription = "Product image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),

                )
            Text(
                text = item.title,
                style = BOLD_STYLE,
                color = Color.Black
                ,
                modifier = Modifier.padding(top = 5.dp)
            )
            Text(
                text = buildAnnotatedString {
                    withStyle(BOLD_STYLE.toSpanStyle()){
                        append("Price: ")
                    }
                    withStyle(NORMAL_STYLE.toSpanStyle()){
                        append(item.price.toString())
                    }
                },
                modifier = Modifier.padding(top = 5.dp)
            )
            Text(
                text = item.description,
                style = NORMAL_STYLE,
                color = Color.Black,
                maxLines = 3,
                modifier = Modifier.padding(top = 5.dp)
            )
        }
    }
}


