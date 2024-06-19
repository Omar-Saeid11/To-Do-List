package com.example.todolist

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.todolist.navigation.AppNavGraph

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun NotesApp(startDestination: String) {
    val navController = rememberNavController()
    Scaffold(
    ) {
        AppNavGraph(navController = navController, startDestination = startDestination)
    }
}
