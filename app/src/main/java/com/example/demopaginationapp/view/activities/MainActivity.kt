package com.example.demopaginationapp.view.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.demopaginationapp.view.screens.DetailScreen
import com.example.demopaginationapp.view.screens.ListScreen
import com.example.demopaginationapp.view.screens.ProductDetailScreen
import com.example.demopaginationapp.view.screens.ProductScreen
import com.example.demopaginationapp.view.theme.DemoPaginationAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint  //annotation required for composables to be able to get viewmodel dependency using hiltViewModel()
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DemoPaginationAppTheme {
                    AppNavHostSetup()
            }
        }
    }


    @Composable
    fun AppNavHostSetup() {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = "product_screen"  //set the start destination - the first visible default fragment
        ) {
            composable("list_screen") { //used as key for navigation
                ListScreen(navController)       //navigate to class
            }
            composable("product_screen") { //used as key for navigation
                ProductScreen(navController)       //navigate to class
            }
            composable(
                route = "detail_screen/{data}", // Define the argument name
                arguments = listOf(navArgument("data") {
                    type = NavType.StringType           // Define the type
                })
            ) { backStackEntry ->
                val data = backStackEntry.arguments?.getString("data")
                Log.d("jfbjbfwehjbefw", "AppNavHostSetup: $data")
                DetailScreen(data = data, navController) // Pass the data to the DetailScreen and convert there

            }
            composable(
                route = "product_detail_screen/{data}", // Define the argument name
                arguments = listOf(navArgument("data") {
                    type = NavType.StringType           // Define the type
                })
            ) { backStackEntry ->
                val data = backStackEntry.arguments?.getString("data")
                Log.d("jfbjbfwehjbefw", "AppNavHostSetup: $data")
                ProductDetailScreen(data = data, navController) // Pass the data to the DetailScreen and convert there

            }
        }
    }


    @Composable
    fun TabNavigation(){
        val navController = rememberNavController()
//        val startDestination = Destination.SONGS
//        var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }



    }
}

