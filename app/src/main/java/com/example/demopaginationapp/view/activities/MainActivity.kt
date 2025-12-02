package com.example.demopaginationapp.view.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.demopaginationapp.navigation.AppNavHostSetup
import com.example.demopaginationapp.navigation.bottomBarRoutes
import com.example.demopaginationapp.view.screens.BottomNavigationBar
import com.example.demopaginationapp.view.theme.DemoPaginationAppTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint  //annotation required for composables to be able to get viewmodel dependency using hiltViewModel()
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            DemoPaginationAppTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                Scaffold(
                    // Bottom navigation
                    bottomBar = {
                        if (currentRoute in bottomBarRoutes)
                            BottomNavigationBar(navController = navController)
                    },
                ){ paddingValues ->
                    // Nav host
                    AppNavHostSetup(navController = navController, padding = paddingValues)
                }
            }
        }
    }
}






