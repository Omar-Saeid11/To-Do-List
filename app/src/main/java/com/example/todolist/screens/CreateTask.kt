package com.example.todolist.screens


import android.graphics.Bitmap
import android.icu.util.Calendar
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.todolist.composables.BackgroundColorSelection
import com.example.todolist.composables.DateSelection
import com.example.todolist.model.TaskModel
import com.example.todolist.viewmodel.TaskViewModel
import java.io.ByteArrayOutputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun CreateTaskScreen(
    navController: NavController,
    viewModel: TaskViewModel = hiltViewModel(),
    taskId: Int? = null
) {
    var noteTitle by remember { mutableStateOf("") }
    var noteSubtitle by remember { mutableStateOf("") }
    var noteText by remember { mutableStateOf("") }
    var noteImageUrl by remember { mutableStateOf<String?>(null) }
    var noteWebUrl by remember { mutableStateOf<String?>(null) }
    var selectedColor by remember { mutableStateOf(Color(0xFF0F1475)) }
    var selectedDate by remember { mutableStateOf(Calendar.getInstance()) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedImageByteArray by remember { mutableStateOf<ByteArray?>(null) }
    var selectedPriority by remember { mutableIntStateOf(1) }
    var categoryDropdownExpanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf("Other") }

    val scrollState = rememberScrollState()
    val context = LocalContext.current

    val taskById by if (taskId != null) {
        viewModel.getTaskById(taskId).collectAsState(initial = null)
    } else {
        remember {
            mutableStateOf(null)
        }
    }

    LaunchedEffect(taskById) {
        taskById?.let { task ->
            noteTitle = task.title ?: ""
            noteSubtitle = task.subtitle ?: ""
            noteText = task.noteText ?: ""
            noteImageUrl = task.imagePath
            noteWebUrl = task.webLink ?: ""
            selectedColor = Color(task.colorArgb)
            selectedImageByteArray = task.imageByteArray
            selectedPriority = task.priority
            selectedCategory = task.category ?: "Other"

            task.dateTime?.let { dateString ->
                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                try {
                    val date = sdf.parse(dateString)
                    date?.let {
                        selectedDate.time = it
                    }
                } catch (e: ParseException) {
                    Log.e("CreateTaskScreen", "Error parsing date: $dateString", e)
                }
            }
        }
    }

    val getContent = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        selectedImageUri?.let { uri ->
            noteImageUrl = uri.toString()

            val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            selectedImageByteArray = byteArrayOutputStream.toByteArray()
        }
    }

    fun selectImageFromGallery() {
        getContent.launch("image/*")
    }

    val textColor = Color.White

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(selectedColor)
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = textColor
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = {
                    if (taskId != null) {
                        // Update existing task
                        val updatedTask = TaskModel(
                            id = taskId,
                            title = noteTitle,
                            subtitle = noteSubtitle,
                            noteText = noteText,
                            imagePath = noteImageUrl,
                            imageByteArray = selectedImageByteArray,
                            webLink = noteWebUrl,
                            category = selectedCategory,
                            colorArgb = selectedColor.toArgb(),
                            dateTime = formatDate(selectedDate),
                            priority = selectedPriority
                        )
                        viewModel.updateTask(updatedTask)
                        viewModel.refreshAllTasks()
                    } else {
                        // Create new task
                        val newTask = TaskModel(
                            title = noteTitle,
                            subtitle = noteSubtitle,
                            noteText = noteText,
                            imagePath = noteImageUrl,
                            imageByteArray = selectedImageByteArray,
                            webLink = noteWebUrl,
                            category = selectedCategory,
                            colorArgb = selectedColor.toArgb(),
                            dateTime = formatDate(selectedDate),
                            priority = selectedPriority
                        )
                        viewModel.addTask(newTask)
                    }
                    navController.popBackStack()
                }) {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Save",
                        tint = textColor,
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(selectedColor)
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = noteTitle,
                    onValueChange = { noteTitle = it },
                    modifier = Modifier
                        .fillMaxWidth(),
                    label = {
                        Text(
                            text = "Title",
                            color = textColor
                        )
                    },
                    textStyle = LocalTextStyle.current.copy(fontSize = 16.sp, color = textColor),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { }),

                    )

                OutlinedTextField(
                    value = noteSubtitle,
                    onValueChange = { noteSubtitle = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = "Subtitle",
                            color = textColor
                        )
                    },
                    textStyle = LocalTextStyle.current.copy(fontSize = 16.sp, color = textColor),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { }),
                )

                BackgroundColorSelection(selectedColor = selectedColor) { color ->
                    selectedColor = color
                }

                DateSelection(selectedDate = selectedDate) { dateInMillis ->
                    selectedDate.timeInMillis = dateInMillis
                }

                OutlinedTextField(
                    value = noteWebUrl ?: "",
                    onValueChange = { noteWebUrl = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = "URL",
                            color = textColor
                        )
                    },
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 13.sp,
                        color = textColor
                    )
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { selectImageFromGallery() },
                        modifier = Modifier.padding(vertical = 8.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                    ) {
                        Text(text = "Select Photo", color = Color.Blue)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    selectedImageUri?.let { uri ->
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = "Selected Image",
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .size(50.dp)
                                .clip(MaterialTheme.shapes.medium),
                            contentScale = ContentScale.Crop,
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Category:",
                        color = textColor,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Box(
                        modifier = Modifier
                            .clickable(onClick = {
                                categoryDropdownExpanded = !categoryDropdownExpanded
                            })
                            .background(
                                color = Color.LightGray,
                                shape = MaterialTheme.shapes.medium
                            )
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = selectedCategory,
                            color = Color.Blue,
                            fontSize = 16.sp
                        )
                        DropdownMenu(
                            expanded = categoryDropdownExpanded,
                            onDismissRequest = { categoryDropdownExpanded = false }
                        ) {
                            listOf("University", "Home", "Work", "Other").forEach { category ->
                                DropdownMenuItem(onClick = {
                                    selectedCategory = category
                                    categoryDropdownExpanded = false
                                }) {
                                    Text(text = category, color = Color.Blue)
                                }
                            }
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Priority:",
                        color = textColor,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Slider(
                        value = selectedPriority.toFloat(),
                        onValueChange = { value ->
                            selectedPriority = value.toInt()
                        },
                        valueRange = 1f..5f,
                        steps = 4,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = selectedPriority.toString(),
                        color = textColor,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .height(200.dp),
                    label = {
                        Text(
                            text = "Task notes",
                            color = textColor
                        )
                    },
                    textStyle = LocalTextStyle.current.copy(fontSize = 13.sp, color = textColor),
                    maxLines = 10,
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.None)
                )
            }
        }
    }
}

fun formatDate(calendar: Calendar): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return sdf.format(calendar.time)
}