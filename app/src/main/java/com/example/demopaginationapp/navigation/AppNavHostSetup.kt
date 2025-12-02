package com.example.demopaginationapp.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.demopaginationapp.view.screens.BrandScreen
import com.example.demopaginationapp.view.screens.CartScreen
import com.example.demopaginationapp.view.screens.CategoriesScreen
import com.example.demopaginationapp.view.screens.CouponScreen
import com.example.demopaginationapp.view.screens.DetailScreen
import com.example.demopaginationapp.view.screens.FavScreen
import com.example.demopaginationapp.view.screens.HomeScreen
import com.example.demopaginationapp.view.screens.ListScreen
import com.example.demopaginationapp.view.screens.ProductDetailScreen
import com.example.demopaginationapp.view.screens.ProductScreen
import com.example.demopaginationapp.view.screens.SearchScreen

@Composable
fun AppNavHostSetup(navController: NavHostController, padding: PaddingValues) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()  //observe back stack entry and cause recomposition when needed to update ui outside the nav base dn the current route
    val currentRoute = navBackStackEntry?.destination?.route  //need current state to update scaffold padding
    NavHost(
        modifier = Modifier.background(color = Color.White).padding(padding)
          /*  if (currentRoute in bottomBarRoutes)
            Modifier.background(color = Color.White).padding(padding)
        else
            Modifier.background(color = Color.White)*/
        ,
        navController = navController,
        startDestination = Screens.Home  //set the start destination - the first visible default fragment
    ) {
        composable(Screens.ReposList) { //used as key for navigation
            ListScreen(navController)       //navigate to class
        }
        composable(Screens.Brands) { //used as key for navigation
            BrandScreen(navController)       //navigate to class
        }
        composable(Screens.Search) { //used as key for navigation
            SearchScreen(navController)       //navigate to class
        }
        composable(Screens.Favorites) { //used as key for navigation
            FavScreen(navController)       //navigate to class
        }
        composable(Screens.Home) { //used as key for navigation
            HomeScreen(navController)       //navigate to class
        }
        composable(Screens.ProductsList) { //used as key for navigation
            ProductScreen(navController)       //navigate to class
        }
        composable(Screens.Coupons) { //used as key for navigation
            CouponScreen(navController)       //navigate to class
        }
        composable(Screens.Cart) { //used as key for navigation
            CartScreen(navController)       //navigate to class
        }
        composable(Screens.Categories) { //used as key for navigation
            CategoriesScreen()       //navigate to class
        }
        composable(
            route = "${Screens.RepoDetail}/{data}", // Define the argument name
            arguments = listOf(navArgument("data") {
                type = NavType.StringType           // Define the type
            })
        ) { backStackEntry ->
            val data = backStackEntry.arguments?.getString("data")
            DetailScreen(data = data, navController) // Pass the data to the DetailScreen
        }
        composable(
            route = "${Screens.ProductDetail}/{data}", // Define the argument name
            arguments = listOf(navArgument("data") {
                type = NavType.StringType           // Define the type
            })
        ) { backStackEntry ->
            val data = backStackEntry.arguments?.getString("data")
            ProductDetailScreen(data = data, navController) // Pass the data to DetailScreen

        }
    }
}
