package com.example.todolist.screens.home


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.todolist.composables.ButtonAddTask
import com.example.todolist.composables.EmptyHome
import com.example.todolist.composables.SearchBar
import com.example.todolist.composables.TaskList
import com.example.todolist.navigation.Screen
import com.example.todolist.viewmodel.TaskViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavController, viewModel: TaskViewModel = hiltViewModel()) {

    val allTasks by viewModel.allTasks.collectAsState()
    val completedTasks by viewModel.completedTasks.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val scope = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.refreshAllTasks()
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val refreshTrigger = rememberUpdatedState(navBackStackEntry)

    LaunchedEffect(refreshTrigger.value) {
        if (refreshTrigger.value != null) {
            viewModel.refreshAllTasks()
        }
    }

    LaunchedEffect(allTasks, completedTasks) {
        isLoading = allTasks.isEmpty() && completedTasks.isEmpty()
    }

    LaunchedEffect(isLoading) {
        if (isLoading) {
            delay(2000)
            isLoading = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(60.dp)
                        .padding(16.dp)
                )
            } else {
                if (allTasks.isNotEmpty() || completedTasks.isNotEmpty()) {
                    TopAppBar(
                        title = {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentSize(Alignment.Center)
                            ) {
                                Text("Tasks", color = Color.White)
                            }
                        },
                        backgroundColor = Color.Black,
                    )
                    SearchBar(
                        searchQuery = searchQuery,
                        onSearchQueryChange = { viewModel.onSearchQueryChange(it) }
                    )
                }

                if (allTasks.isEmpty() && completedTasks.isEmpty()) {
                    EmptyHome()
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .weight(1f)
                    ) {
                        if (allTasks.any {
                                !it.isCompleted && it.title?.contains(
                                    searchQuery,
                                    ignoreCase = true
                                ) == true
                            }) {
                            Text(
                                "Latest",
                                color = Color.White,
                                style = MaterialTheme.typography.h6
                            )

                            TaskList(
                                tasks = allTasks.filter {
                                    !it.isCompleted && it.title?.contains(
                                        searchQuery,
                                        ignoreCase = true
                                    ) == true
                                },
                                onDeleteTask = { task ->
                                    run {
                                        viewModel.deleteTask(task)
                                        scope.launch {
                                            isLoading = true
                                            delay(2000)
                                            viewModel.refreshAllTasks()
                                            isLoading = false
                                        }
                                    }
                                },
                                onEditTask = { task -> navController.navigate("editTask/${task.id}") },
                                onCompleteTask = { task ->
                                    run {
                                        viewModel.markTaskAsCompleted(task)
                                        scope.launch {
                                            isLoading = true
                                            delay(2000)
                                            viewModel.refreshAllTasks()
                                            isLoading = false
                                        }
                                    }
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        if (completedTasks.any {
                                it.title?.contains(
                                    searchQuery,
                                    ignoreCase = true
                                ) == true
                            }) {
                            Text(
                                "Completed",
                                color = Color.White,
                                style = MaterialTheme.typography.h6
                            )

                            TaskList(
                                tasks = completedTasks.filter {
                                    it.title?.contains(
                                        searchQuery,
                                        ignoreCase = true
                                    ) == true
                                },
                                onDeleteTask = { task -> viewModel.deleteTask(task) },
                                onEditTask = { task -> navController.navigate("editTask/${task.id}") },
                                onCompleteTask = { task -> viewModel.markTaskAsCompleted(task) }
                            )
                        }
                    }
                }
            }
        }

        ButtonAddTask(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onFabClick = { navController.navigate(Screen.CreateTaskScreen.route) }
        )
    }
}
