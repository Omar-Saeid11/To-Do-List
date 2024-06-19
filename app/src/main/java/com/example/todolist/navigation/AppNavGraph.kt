package com.example.todolist.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.todolist.screens.CreateTaskScreen
import com.example.todolist.screens.OnBoardingScreen
import com.example.todolist.screens.home.HomeScreen

@Composable
fun AppNavGraph(navController: NavHostController, startDestination: String) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.OnBoardingScreen.route) { OnBoardingScreen(navController) }
        composable(Screen.HomeScreen.route) { HomeScreen(navController = navController) }
        composable(Screen.CreateTaskScreen.route) { CreateTaskScreen(navController = navController) }
        composable(
            route = "editTask/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId")
            taskId?.let {
                CreateTaskScreen(navController = navController, taskId = taskId)
            }
        }
    }
}





