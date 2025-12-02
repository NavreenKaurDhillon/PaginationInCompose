package com.example.demopaginationapp.view.screens

import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.demopaginationapp.R
import com.example.demopaginationapp.model.dataclasses.Product
import com.example.demopaginationapp.navigation.Screens
import com.example.demopaginationapp.utils.BOLD_STYLE
import com.example.demopaginationapp.utils.NORMAL_STYLE
import com.example.demopaginationapp.viewmodel.ProductViewModel

@Composable
fun CartScreen(navController: NavHostController) {

    val context = LocalContext.current
    val activity = context as ComponentActivity
    val productViewModel: ProductViewModel = hiltViewModel(viewModelStoreOwner = activity)
    var sum = 0.0
    for (a in productViewModel.cartProducts)
        sum += a.price

    BackHandler(enabled = true) {
        //Handle the back press manually -> navigate to home
        navController.navigate(Screens.Home) {
            popUpTo(Screens.Home) {
                inclusive = true  // remove other entries and goto home
            }
            launchSingleTop = true
        }
    }

    Column(modifier = Modifier.padding(horizontal = 15.dp).fillMaxSize()) {
        Text(text = "My Cart", style = BOLD_STYLE, fontSize = 20.sp, modifier =  Modifier.padding( 10.dp).fillMaxWidth(), textAlign = TextAlign.Center)
        val scrollState = rememberScrollState()
            if (productViewModel.cartProducts.isNotEmpty()) {
                Column(modifier = Modifier.verticalScroll(scrollState).fillMaxSize()
                ) {
                    productViewModel.cartProducts.forEachIndexed { index, cartProduct ->
                        CartItemCard(cartProduct, productViewModel)
                        if (index < productViewModel.cartProducts.size - 1) {
                            Divider(color = Color.LightGray, thickness = 1.dp, modifier = Modifier.padding(horizontal = 15.dp, vertical = 15.dp))
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                    Row(modifier = Modifier.padding(15.dp).fillMaxWidth()
                            .shadow(elevation = 4.dp, shape = MaterialTheme.shapes.large)
                            .background(color = Color.White, shape = MaterialTheme.shapes.large)
                            .clickable(onClick = {
                                    //open coupons screen
                                    navController.navigate(Screens.Coupons)
                                },
                            ),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(Modifier.width(15.dp))
                        Image(painter = painterResource(R.drawable.discount), contentDescription = "Coupon", Modifier.size(25.dp))
                        Text(text = "Apply Coupon", style = BOLD_STYLE, modifier = Modifier.padding(vertical = 15.dp), fontSize = 18.sp)
                    }
                    Spacer(Modifier.height(20.dp))
                    Column(modifier = Modifier.padding(15.dp)) {
                        Text(text = "Order Details", style = BOLD_STYLE, fontSize = 20.sp)
                        Divider(modifier = Modifier.padding(vertical = 8.dp),
                            thickness = 2.dp, // Thicker line
                            color = Color.LightGray  // Custom color
                                     )
                        Row {
                            Text(text = "Items", style = NORMAL_STYLE, color = Color.Gray, modifier = Modifier.weight(0.5f),
                                fontSize = 18.sp)
                            Text(text = productViewModel.cartProducts.size.toString(),
                                style = NORMAL_STYLE, fontSize = 18.sp, textAlign = TextAlign.End,
                                modifier = Modifier.weight(0.5f).padding(top = 6.dp))
                        }
                        Row {
                            Text(text = "Total", style = BOLD_STYLE, fontSize = 18.sp,
                                color = Color.Gray, modifier = Modifier.weight(0.5f))
                            Text(text = "$ ${String.format("%.2f", sum)}", style = BOLD_STYLE,
                                textAlign = TextAlign.End, fontSize = 18.sp, modifier = Modifier.weight(0.5f).padding(top = 6.dp))
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                    TextButton(
                        modifier = Modifier.background(color = Color.Blue),
                        onClick = {
                            Toast.makeText(context, "Order placed successfully", Toast.LENGTH_LONG).show()
                            navController.navigate(Screens.Home)
                        }, contentPadding = PaddingValues(10.dp)
                    ) {
                        Text(text = "PROCEED TO CHECKOUT", style = BOLD_STYLE, color = Color.White, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No products found!", style = NORMAL_STYLE, textAlign = TextAlign.Center)
                }
            }
    }
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CartItemCard(item: Product, productViewModel: ProductViewModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        GlideImage(model = item.images[0], contentDescription = item.title, modifier = Modifier.size(100.dp))
        Column(modifier = Modifier.weight(1f).padding(start = 15.dp)
        ) {
            Text(text = item.title, style = BOLD_STYLE, color = Color.Black, modifier = Modifier.padding(top = 4.dp))
            Text(text = "Brand: ${item.brand?:"Dummy"}", style = NORMAL_STYLE, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
            Text(text = "$ ${item.price}", style = BOLD_STYLE, color = Color.Black, modifier = Modifier.padding(top = 4.dp))
        }

        IconButton(onClick = { productViewModel.cartProducts.remove(item) }) {
            Image(painter = painterResource(R.drawable.baseline_delete_24), modifier = Modifier.size(20.dp), contentDescription = "Delete cart product")
        }
    }
}