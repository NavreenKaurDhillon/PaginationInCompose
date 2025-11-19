package com.example.demopaginationapp.view.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.demopaginationapp.view.theme.DemoPaginationAppTheme
import com.example.demopaginationapp.viewmodel.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<BaseViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DemoPaginationAppTheme {
                Scaffold(modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)) { innerPadding ->
                    DisplayItems(viewModel, innerPadding)
//                    showItem()
                }
            }
        }
    }
}



@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun DisplayItems(viewModel: BaseViewModel, innerPadding: PaddingValues) {
    val lazyPagingItems = viewModel.pagingDataFlow.collectAsLazyPagingItems() //add observer that causes recomposition when there is an update in paging items
    //collectAsLazyPagingItems() is used to bind the UI to the paging so it automatically sets data when we get new page response
    //also trigger the paging library to implement the logic when user has scrolled down
    Column {

            Text(
                text = "Names List", style = TextStyle(
                    fontWeight = FontWeight.Bold, fontFamily = FontFamily.SansSerif,
                    fontSize = 18.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(). padding(top = 20.dp)
            )

        LazyColumn(modifier = Modifier
            .padding(top = 0.dp, bottom = 20.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth()) {
            items(lazyPagingItems){ responseDataItem ->
                // items() is a extension function that converts paging data into set of objects that can be used to display data to UI
                if (responseDataItem != null) {
                    // This is the individual item Composable
                    ElevatedCard(modifier = Modifier
                        .padding(vertical = 10.dp, horizontal = 5.dp)
                        .fillMaxWidth()) {
                        Row(modifier = Modifier.padding(vertical = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                            GlideImage(
                                model = responseDataItem.owner.avatar_url,
                                contentDescription = "Logo description of the repo",
                                modifier = Modifier.padding(6.dp, )
                                    .width(40.dp)
                                    .height(40.dp),

                            )
                            Text(
                                text = "Name: ",
                                modifier = Modifier.padding(start = 8.dp),
                                color = Color.Black // Assuming a dark text color,
                                , style = TextStyle(fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.SansSerif,
                                    fontSize = 16.sp))
                            Text(
                                text = responseDataItem.name,
                                modifier = Modifier.padding(start = 5.dp),
                                color = Color.Black // Assuming a dark text color,
                                , style = TextStyle(fontWeight = FontWeight.Normal,
                                    fontFamily = FontFamily.SansSerif,
                                    fontSize = 16.sp))
                        }

                    }

                } else {
                    // Placeholder for items that haven't loaded yet (if placeholders are enabled)
                    Text(text = "Loading Item...", color = Color.Gray)
                }
            }
            lazyPagingItems.apply {
                //paging 3 has built in error handling to update the UI accordingly
                when {
                    loadState.refresh is LoadState.Loading -> { //refresh is called during initial load or full refresh
                        //has two states - loading , error
                        //show progress while waiting for the data
                        item {
                            Box(modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp)) {
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
                            Box(modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp)) {
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