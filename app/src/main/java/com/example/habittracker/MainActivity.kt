package com.example.habittracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.habittracker.ui.habit.HabitScreen
import com.example.habittracker.ui.home.HomeScreen
import com.example.habittracker.ui.theme.HabitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HabitTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
private fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomeScreen(onHabitClick = { id -> navController.navigate("habit/$id") })
        }
        composable(
            "habit/{habitId}",
            arguments = listOf(navArgument("habitId") { type = NavType.LongType }),
        ) {
            HabitScreen(onBack = { navController.popBackStack() })
        }
    }
}
