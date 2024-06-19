package com.example.todolist.navigation

sealed class Screen(val route: String) {
    data object OnBoardingScreen : Screen("OnBoardingScreen")
    data object HomeScreen : Screen("HomeScreen")
    data object CreateTaskScreen:Screen("createTask")
}