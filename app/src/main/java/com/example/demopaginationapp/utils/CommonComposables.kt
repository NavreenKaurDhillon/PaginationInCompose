package com.example.demopaginationapp.utils

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.demopaginationapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(name:String, showBackButton: Boolean, navController: NavController) {
  return  TopAppBar(
        title = { Text(name, style = BOLD_STYLE, fontSize = 20.sp) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        ),
        navigationIcon = {
            if(showBackButton) {
                IconButton(onClick = {
                    // go back to last screen
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, // Standard back arrow icon
                        contentDescription = "Go back",
                        tint = Color.Black,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        },
      actions = {
          if(!showBackButton) {
              IconButton(onClick = {
                  navController.navigate("list_screen")
              }) {
                  Image(
                     painter = painterResource(R.drawable.outline_event_list_24), // Standard back arrow icon
                      contentDescription = "Go back",
                      modifier = Modifier.size(30.dp)
                  )
              }
          }
      })
}


@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CustomGlideImage(
    image: String,
    sizeDp: Dp,
    paddingDp: Dp,
    elevationDp: Dp,
    imageShape: RoundedCornerShape
){
    return  GlideImage(
        model = image,
        contentDescription = "img",
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingDp)
            .shadow(
                elevation = elevationDp, // Use a noticeable elevation for testing
                shape = imageShape)
            .background(Color.White, shape = imageShape)
            .clip(imageShape),
    )
}
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun RounderRecGlideImage(
    image: String,
    sizeDp: Dp = 0.dp
){
    return   GlideImage(
        model = image,
        contentDescription = "Logo description of the repo",
        modifier = if (sizeDp == 0.dp){
            Modifier
                .fillMaxSize()
                .padding( 8.dp)
                .shadow(
                    elevation = 4.dp, // Use a noticeable elevation for testing
                    shape = RoundedCornerShape(15.dp)).background(Color.White, shape = RoundedCornerShape(15.dp))
                .clip(RoundedCornerShape(15.dp))
        } else {
            Modifier
                .height(sizeDp)
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .shadow(
                    elevation = 4.dp, // Use a noticeable elevation for testing
                    shape = RoundedCornerShape(15.dp)).background(Color.White, shape = RoundedCornerShape(15.dp))
                .clip(RoundedCornerShape(15.dp))
        },
        contentScale = ContentScale.Fit)
}