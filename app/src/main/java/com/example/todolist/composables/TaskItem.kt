package com.example.todolist.composables

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todolist.model.TaskModel

@Composable
fun TaskItem(
    task: TaskModel,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
    onCompleteClick: () -> Unit,
) {
    val backgroundColor = remember { Color(task.colorArgb) }
    val textColor = Color.Black
    val selectedTextColor = Color.White
    var isExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val onItemClicked = { isExpanded = !isExpanded }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(
                if (isExpanded) backgroundColor else backgroundColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onItemClicked)
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            if (isExpanded) {
                Text(
                    text = task.title ?: "No Title",
                    style = MaterialTheme.typography.h6.copy(color = selectedTextColor),
                    fontSize = 18.sp,
                    maxLines = 1,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(Color.Transparent, CircleShape)
                        .border(2.dp, if (isExpanded) selectedTextColor else textColor, CircleShape)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    if (!isExpanded) {
                        Text(
                            text = task.title ?: "No Title",
                            style = MaterialTheme.typography.h6.copy(color = textColor),
                            fontSize = 18.sp,
                            maxLines = 1
                        )
                    }

                    if (!isExpanded) {
                        Text(
                            text = task.dateTime ?: "No Date",
                            style = MaterialTheme.typography.body2.copy(color = if (isExpanded) selectedTextColor else textColor),
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                }

                Spacer(modifier = Modifier.width(16.dp))
                CategoryBadge(category = task.category)
                Spacer(modifier = Modifier.width(16.dp))
                PriorityBadge(priority = task.priority)

                if (isExpanded) {
                    Spacer(modifier = Modifier.width(12.dp))

                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.White,
                        modifier = Modifier
                            .clickable(onClick = onDeleteClick)
                            .size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color.White,
                        modifier = Modifier
                            .clickable(onClick = onEditClick)
                            .size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Icon(
                        imageVector = Icons.Default.Done,
                        contentDescription = "Complete",
                        tint = Color.White,
                        modifier = Modifier
                            .clickable(onClick = onCompleteClick)
                            .size(24.dp)
                    )
                }
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = task.noteText ?: "No Notes",
                    style = MaterialTheme.typography.body1.copy(color = textColor),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                task.imageByteArray?.let { byteArray ->
                    val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Task Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(shape = RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.FillBounds
                    )
                }

                task.webLink?.let { webUrl ->
                    Column {
                        Spacer(modifier = Modifier.height(8.dp))

                        ClickableText(
                            text = buildAnnotatedString { append(webUrl) },
                            style = MaterialTheme.typography.body2.copy(
                                color = Color.White,
                                textDecoration = TextDecoration.Underline
                            ),
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(webUrl))
                                context.startActivity(intent)
                            },
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        Text(
                            text = task.dateTime ?: "No Date",
                            style = MaterialTheme.typography.body2.copy(color = if (isExpanded) selectedTextColor else textColor),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
