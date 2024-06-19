package com.example.todolist.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.db.TaskRepository
import com.example.todolist.model.TaskModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val repository: TaskRepository) : ViewModel() {

    private val _allTasks = MutableStateFlow<List<TaskModel>>(emptyList())
    private val _completedTasks = MutableStateFlow<List<TaskModel>>(emptyList())
    private val _searchQuery = MutableStateFlow("")

    val allTasks: StateFlow<List<TaskModel>> = _allTasks
    val completedTasks: StateFlow<List<TaskModel>> = _completedTasks
    val searchQuery: StateFlow<String> = _searchQuery

    init {
        getAllTasks()
    }

    private fun getAllTasks() {
        viewModelScope.launch {
            try {
                repository.getAllTasks().collect { tasks ->
                    _allTasks.value = tasks.filter { !it.isCompleted }
                    _completedTasks.value = tasks.filter { it.isCompleted }
                }
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Error fetching all tasks", e)
            }
        }
    }

    fun refreshAllTasks() {
        getAllTasks()
    }

    fun addTask(task: TaskModel) {
        viewModelScope.launch {
            try {
                repository.addTask(task)
                refreshAllTasks()
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Error adding task: ${task.title}", e)
            }
        }
    }

    fun deleteTask(task: TaskModel) {
        viewModelScope.launch {
            try {
                repository.deleteTask(task)
                refreshAllTasks()
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Error deleting task: ${task.title}", e)
            }
        }
    }

    fun updateTask(task: TaskModel) {
        viewModelScope.launch {
            try {
                repository.updateTask(task)
                refreshAllTasks()
            } catch (e: Exception) {
                Log.e("TaskViewModel", "Error updating task: ${task.title}", e)
            }
        }
    }

    fun getTaskById(taskId: Int): Flow<TaskModel?> {
        return repository.getTaskById(taskId)
    }

    fun markTaskAsCompleted(task: TaskModel) {
        viewModelScope.launch {
            repository.markTaskAsCompleted(task)
            refreshAllTasks()
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }
}
