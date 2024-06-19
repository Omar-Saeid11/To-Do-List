package com.example.todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.todolist.navigation.Screen
import com.example.todolist.ui.theme.ToDoListTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        val startDestination = if (isFirstTimeOpeningApp()) {
            Screen.OnBoardingScreen.route
        } else {
            Screen.HomeScreen.route
        }

        setContent {
            ToDoListTheme {
                NotesApp(startDestination = startDestination)
            }
        }
    }

    private fun isFirstTimeOpeningApp(): Boolean {
        return !PreferenceManager.isLoggedIn(this)
    }
}
