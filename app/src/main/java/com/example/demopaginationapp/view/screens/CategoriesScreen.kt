package com.example.demopaginationapp.view.screens

import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.demopaginationapp.R
import com.example.demopaginationapp.model.dataclasses.CategoriesResponseData
import com.example.demopaginationapp.model.networking.Resource
import com.example.demopaginationapp.model.networking.Status
import com.example.demopaginationapp.utils.BOLD_STYLE
import com.example.demopaginationapp.utils.NORMAL_STYLE
import com.example.demopaginationapp.utils.RounderRecGlideImage
import com.example.demopaginationapp.utils.SMALL_BOLD_STYLE
import com.example.demopaginationapp.viewmodel.CategoryViewmodel

@Composable
fun CategoriesScreen() {

    val context = LocalContext.current
    val activity = context as ComponentActivity
    val viewmodel: CategoryViewmodel = hiltViewModel(viewModelStoreOwner = activity)
    val resource by viewmodel.categoriesList.observeAsState(initial = Resource.loading(null))
    val state = resource
    val subCategoriesResource by viewmodel.subCategoriesList.observeAsState(initial = Resource.loading(null))
    val subCategoriesState = subCategoriesResource

    when (resource.status) {
        Status.LOADING -> {
            Log.d("elwkhkjwehkjehw", "LOADING: resource")
              Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator() }
        }
        Status.SUCCESS -> {
            val data = state.data
            ShowCategories(data, subCategoriesState = subCategoriesState, viewmodel = viewmodel)
            Log.d("reryergfddfgd", "CategoriesScreen: $subCategoriesState")
            LaunchedEffect(Unit) { viewmodel.getSubCategories(data?.data?.categories?.get(0)?.prodcat_id?.toInt() ?: 0) }
        }
        Status.ERROR -> {
            // Show the error message
            Text(text = "Failed to load products: ${state.message}", color = Color.Red, modifier = Modifier.padding(16.dp))
        }
        else -> {}
    }
}


@Composable
fun ShowCategories(
    data: CategoriesResponseData?,
    subCategoriesState: Resource<CategoriesResponseData>,
    viewmodel: CategoryViewmodel
) {
    var selectedId by remember { mutableStateOf(data?.data?.categories?.get(0)?.prodcat_id) }
    Column(Modifier.fillMaxSize()) {
        Text(text = "Shopping Categories", style = BOLD_STYLE, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontSize = 20.sp)
        Spacer(Modifier.height(20.dp))
        Row(modifier = Modifier.background(color = Color.White).fillMaxWidth()) {
            LazyColumn(modifier = Modifier.weight(0.25f).background(color = colorResource(R.color.light_blue))
                .padding(start = 12.dp).fillMaxHeight(), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                items(data?.data?.categories?.size ?: 0) { pos ->
                    val item = data?.data?.categories
                    Column(modifier = Modifier.fillMaxWidth().background(
                                color = if (data?.data?.categories?.get(pos)?.prodcat_id == selectedId) Color.White else colorResource(
                                    R.color.light_blue))
                            .clickable {
                                selectedId = data?.data?.categories?.get(pos)?.prodcat_id
                                viewmodel.getSubCategories(data?.data?.categories?.get(pos)?.prodcat_id?.toInt() ?: 0)
                            },
                        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
                    ) {
                        Spacer(Modifier.height(8.dp))
                        RounderRecGlideImage(item?.get(pos)?.icon ?: "", 70.dp, isSquare = false)
                        Text(text = item?.get(pos)?.prodcat_name ?: "", style = SMALL_BOLD_STYLE, modifier = Modifier.padding(vertical = 10.dp))
                    }
                }
            }

            when (subCategoriesState.status) {
                Status.LOADING -> {
                    Log.d("kejfwbjebwfe", "LOADING: subCategoriesState")
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                }
                Status.SUCCESS -> {
                    Log.d("kejfwbjebwfe", "CategoriesScreen: subCategoriesResource SUCCESS = $data")

                    if (subCategoriesState.data?.data?.categories.isNullOrEmpty()) {
                        Box(modifier = Modifier.weight(0.76f).fillMaxHeight(), contentAlignment = Alignment.Center,) {
                            Text(text = "No products found!", style = NORMAL_STYLE, textAlign = TextAlign.Center)
                        }
                    } else {
                        LazyColumn(modifier = Modifier.weight(0.76f).padding(start = 10.dp, end = 10.dp, bottom = 15.dp ), verticalArrangement = Arrangement.spacedBy(15.dp),
                        ) {
                            items(subCategoriesState.data.data.categories.size) { pos ->
                                val item = subCategoriesState.data.data.categories

                                Column(horizontalAlignment = Alignment.Start) {
                                    ElevatedButton(
                                        onClick = {},
                                        contentPadding = PaddingValues(vertical = 2.dp, horizontal = 10.dp),
                                        modifier = Modifier.fillMaxWidth().background(color = Color.White)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(text = item[pos].prodcat_name, style = BOLD_STYLE)
                                            Spacer(Modifier.fillMaxWidth().weight(1f))
                                            Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "next icon", tint = Color.Black)
                                        }
                                    }
                                    FlowRow(maxItemsInEachRow = 3) {
                                        item[pos].children.forEach {
                                            Column(modifier = Modifier.fillMaxWidth(0.3f).padding(vertical = 10.dp),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                RounderRecGlideImage(it.icon, 70.dp, true, isSquare = false)
                                                Spacer(Modifier.height(8.dp))
                                                Text(text = it.prodcat_name, style = SMALL_BOLD_STYLE, textAlign = TextAlign.Center)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                Status.ERROR -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "No products found!", style = NORMAL_STYLE, textAlign = TextAlign.Center)
                    }
                }
                else -> {}
            }
        }
    }

}