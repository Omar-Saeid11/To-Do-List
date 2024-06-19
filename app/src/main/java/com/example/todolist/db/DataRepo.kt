package com.example.todolist.db


import android.util.Log
import androidx.annotation.WorkerThread
import com.example.todolist.model.TaskModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.withContext

class TaskRepository(private val taskDao: TaskDao) {

    fun getAllTasks(): Flow<List<TaskModel>> {
        return taskDao.getAllTasks()
            .catch { e ->
                Log.e("TaskRepository", "Error fetching all tasks", e)
                emit(emptyList())
            }
    }

    @WorkerThread
    suspend fun addTask(task: TaskModel) {
        withContext(Dispatchers.IO) {
            try {
                taskDao.addTask(task)
            } catch (e: Exception) {
                Log.e("TaskRepository", "Error adding task: ${task.title}", e)
            }
        }
    }

    @WorkerThread
    suspend fun deleteTask(task: TaskModel) {
        withContext(Dispatchers.IO) {
            try {
                taskDao.deleteTask(task)
            } catch (e: Exception) {
                Log.e("TaskRepository", "Error deleting task: ${task.title}", e)
            }
        }
    }

    @WorkerThread
    suspend fun updateTask(task: TaskModel) {
        withContext(Dispatchers.IO) {
            try {
                taskDao.updateTask(task)
            } catch (e: Exception) {
                Log.e("TaskRepository", "Error updating task: ${task.title}", e)
            }
        }
    }

    fun getTaskById(taskId: Int): Flow<TaskModel?> {
        return taskDao.getTaskById(taskId)
            .catch { e ->
                Log.e("TaskRepository", "Error fetching task by ID: $taskId", e)
                emit(null)
            }
    }

    suspend fun markTaskAsCompleted(task: TaskModel) {
        taskDao.updateTask(task.copy(isCompleted = true))
    }
}


