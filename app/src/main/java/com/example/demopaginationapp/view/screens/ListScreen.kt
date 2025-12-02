package com.example.demopaginationapp.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.demopaginationapp.R
import com.example.demopaginationapp.model.dataclasses.ResponseDataItem
import com.example.demopaginationapp.utils.BOLD_STYLE
import com.example.demopaginationapp.utils.NORMAL_STYLE
import com.example.demopaginationapp.utils.SMALL_BOLD_STYLE
import com.example.demopaginationapp.utils.SMALL_NORMAL_STYLE
import com.example.demopaginationapp.viewmodel.BaseViewModel
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(navController: NavHostController) {
    val viewModel: BaseViewModel = hiltViewModel()

    val lazyPagingItems = viewModel.pagingDataFlow.collectAsLazyPagingItems() //add observer that causes recomposition when there is an update in paging items
    //collectAsLazyPagingItems() is used to bind the UI to the paging so it automatically sets data when we get new page response
    //also trigger the paging library to implement the logic when user has scrolled down

//    Scaffold(containerColor = Color.White,
//        topBar = {
//            com.example.demopaginationapp.utils.TopAppBar("Google Repos List", true, navController)}
//         ) { innerPadding ->


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
            Text(text = "Google Repos List", style = BOLD_STYLE, fontSize = 20.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Start)
        }
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 15.dp),
            ) {
                items(lazyPagingItems) { responseDataItem ->
                    // items() is a extension function that converts paging data into set of objects that can be used to display data to UI
                    if (responseDataItem != null) {
                        // This is the individual item Composable
                        ListItemCard(
                            responseDataItem = responseDataItem,
                            navController = navController,
                        )

                    } else {
                        // Placeholder for items that haven't loaded yet (if placeholders are enabled)
//                        Text(text = "Loading Item...", color = Color.Gray)
                    }
                }
                lazyPagingItems.apply {
                    //paging 3 has built in error handling to update the UI accordingly
                    when {
                        loadState.refresh is LoadState.Loading -> { //refresh is called during initial load or full refresh
                            //has two states - loading , error
                            //show progress while waiting for the data
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 20.dp)
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.align(Alignment.Center),
                                        trackColor = Color.Gray
                                    )
                                }
                            }
                        }

                        loadState.append is LoadState.Loading -> { //append is called when user has scrolled to bottom and need to load the next page
                            // append has two states - loading, error (error loading the next page)
                            //show progress
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 20.dp)
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.align(Alignment.Center),
                                        trackColor = Color.Gray
                                    )
                                }
                            }
                        }

                        loadState.append is LoadState.Error -> {
                            //This state is called when there is some error loading the next page
                            //User is informed about the error
                            item { Text("Error loading more items") }
                        }
                    }
                }
        }
    }


}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun ListItemCard(
    responseDataItem: ResponseDataItem,
    navController: NavController
) {


    ElevatedCard(
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 5.dp)
            .fillMaxWidth(), onClick = {

            //send response object
            val jsonString = Gson().toJson(responseDataItem)
            val encodedJson = URLEncoder.encode(jsonString, StandardCharsets.UTF_8.name())
            navController.navigate("detail_screen/$encodedJson")
        },
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White,
        ),
    ) {
        Row(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            GlideImage(
                model = responseDataItem.owner.avatar_url,
                contentDescription = "Logo description of the repo",
                modifier = Modifier
                    .height(100.dp)
                    .height(100.dp))

            Column(modifier = Modifier.weight(1f).padding(horizontal = 10.dp)) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = BOLD_STYLE.toSpanStyle()) {
                                append("ID: ")
                            }
                            withStyle(style = NORMAL_STYLE.toSpanStyle()) {
                                append(responseDataItem.id.toString())
                            }
                        }
                    )
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = BOLD_STYLE.toSpanStyle()){
                            append("Name: ")
                        }
                        withStyle(style = NORMAL_STYLE.toSpanStyle()){
                            append(responseDataItem.name)
                        }
                    }, maxLines = 1)

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 5.dp)) {
                    Image(
                        painter = painterResource(R.drawable.baseline_remove_red_eye_24),
                        modifier = Modifier.size(18.dp),
                        contentDescription = "people rating"
                    )
                    Text(
                        text = responseDataItem.watchers.toString(),
                        modifier = Modifier.padding(
                            top = 2.dp,
                            bottom = 2.dp,
                            start = 5.dp
                        ),
                        style = SMALL_NORMAL_STYLE
                    )

                    Spacer(modifier = Modifier.width(15.dp))
                    Image(
                        painter = painterResource(R.drawable.baseline_people_24),
                        modifier = Modifier.size(18.dp),
                        contentDescription = "people rating"
                    )
                    Text(
                        text = responseDataItem.forks_count.toString(),
                        modifier = Modifier.padding(
                            top = 2.dp,
                            bottom = 2.dp,
                            start = 5.dp
                        ),
                        style = SMALL_NORMAL_STYLE
                    )
                }
                Text(text = buildAnnotatedString {
                    withStyle(style = SMALL_BOLD_STYLE.toSpanStyle()){
                        append("Visibility: ")
                    }
                    withStyle(style = SMALL_NORMAL_STYLE.toSpanStyle()){
                        append(responseDataItem.visibility)
                    }
                })
            }
            Image(
                painter = painterResource(id = R.drawable.baseline_keyboard_arrow_right_24),
                modifier = Modifier.size(30.dp),
                contentDescription = "next icon"
            )

        }

    }
}
