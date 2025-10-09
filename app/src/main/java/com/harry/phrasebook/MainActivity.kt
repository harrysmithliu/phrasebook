package com.harry.phrasebook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.harry.phrasebook.ui.home.HomeScreen
import com.harry.phrasebook.ui.detail.DetailScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val nav = rememberNavController()
                val navigator = rememberAppNavigator(nav)

                NavHost(navController = nav, startDestination = "home") {
                    composable("home") {
                        val vm: MainViewModel = hiltViewModel()
                        HomeScreen(vm, navigator)
                    }
                    composable(
                        route = "detail/{id}",
                        arguments = listOf(
                            navArgument("id") { type = NavType.LongType }
                        )
                    ) { entry ->
                        val id = entry.arguments?.getLong("id") ?: 0L
                        DetailScreen(id = id, onBack = { navigator.back() })
                    }
                }
            }
        }
    }
}
