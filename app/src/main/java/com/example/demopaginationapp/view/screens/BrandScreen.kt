package com.example.demopaginationapp.view.screens

import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.demopaginationapp.navigation.Screens
import com.example.demopaginationapp.utils.BOLD_STYLE
import com.example.demopaginationapp.utils.CustomGlideImage
import com.example.demopaginationapp.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun BrandScreen(navController: NavHostController) {
    val context = LocalContext.current
    val activity = context as ComponentActivity
    val viewModel: ProductViewModel = hiltViewModel(viewModelStoreOwner = activity)

    BackHandler(enabled = true) {
        //Handle the back press manually -> navigate to home
        navController.navigate(Screens.Home) {
            popUpTo(Screens.Home) {
                inclusive = true  // remove other entries and goto home
            }
            launchSingleTop = true
        }
    }

    Column(modifier = Modifier.padding(horizontal = 15.dp)) {
       Text(text = "Shop By Brands", style = BOLD_STYLE, fontSize = 20.sp,  modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        Spacer(Modifier.height(15.dp))
        viewModel.products.value?.data?.products?.let {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(it.size) { item ->
                    Column(modifier = Modifier.clickable{
                        navController.navigate(Screens.ProductsList)
                    }.padding(bottom = 15.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        CustomGlideImage(
                            it[item].images[0],
                            0.dp,
                            8.dp,
                            4.dp,
                            CircleShape)
                        Text(
                            text = it[item].brand?: "Dummy", style = BOLD_STYLE,
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            maxLines = 1, fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }


}