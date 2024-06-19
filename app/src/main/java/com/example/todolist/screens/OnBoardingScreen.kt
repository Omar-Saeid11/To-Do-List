package com.example.todolist.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.todolist.PreferenceManager
import com.example.todolist.composables.ButtonLogin
import com.example.todolist.composables.OnBoardingItem
import com.example.todolist.composables.TopSection
import com.example.todolist.navigation.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScreen(navController: NavController) {
    val items = OnBoardingItems.getData()
    val scope = rememberCoroutineScope()
    val pageState = rememberPagerState(pageCount = { items.size })

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        HorizontalPager(
            state = pageState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            OnBoardingItem(items = items[page])
        }

        TopSection(onBackClick = {
            if (pageState.currentPage > 0) {
                scope.launch {
                    pageState.scrollToPage(pageState.currentPage - 1)
                }
            }
        }, onSkipClick = {
            if (pageState.currentPage < items.size - 1) {
                scope.launch {
                    pageState.scrollToPage(items.size - 1)
                }
            }
        })

        // Login button (visible only on last page)
        if (pageState.currentPage == items.size - 1) {
            ButtonLogin(
                onClick = {
                    navController.navigate(Screen.HomeScreen.route)
                    PreferenceManager.setLoggedIn(navController.context, true)
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )
        }
    }
}