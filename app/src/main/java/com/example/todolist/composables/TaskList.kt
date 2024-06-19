package com.example.todolist.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.todolist.model.TaskModel

@Composable
fun TaskList(
    tasks: List<TaskModel>,
    onDeleteTask: (TaskModel) -> Unit,
    onEditTask: (TaskModel) -> Unit,
    onCompleteTask: (TaskModel) -> Unit
) {
    LazyColumn(modifier = Modifier.padding(16.dp)) {
        item {
            tasks.forEach { task ->
                TaskItem(
                    task = task,
                    onDeleteClick = { onDeleteTask(task) },
                    onEditClick = { onEditTask(task) },
                    onCompleteClick = { onCompleteTask(task) }
                )
                Divider(modifier = Modifier.padding(vertical = 8.dp))
            }
        }
    }
}