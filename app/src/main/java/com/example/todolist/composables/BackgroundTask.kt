package com.example.todolist.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun BackgroundColorSelection(selectedColor: Color, onColorSelected: (Color) -> Unit) {
    val colors = listOf(
        Color.Magenta to "FF00FF",
        Color.Green to "00FF00",
        Color(0xFFFFA500) to "FFA500",
        Color.Blue to "0000FF",
        Color.Red to "FF0000",
        Color.Yellow to "FFFF00"
    )

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Background Color",
            color = Color.Blue,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            colors.forEach { (color) ->
                ColorButton(
                    color = color,
                    isSelected = selectedColor == color,
                    onColorSelected = onColorSelected
                )
            }
        }
    }
}

@Composable
fun ColorButton(
    color: Color,
    isSelected: Boolean,
    onColorSelected: (Color) -> Unit
) {
    val textColor = if (color == Color.Black) Color.White else Color.Black

    Box(
        modifier = Modifier
            .padding(4.dp)
            .size(40.dp)
    ) {
        Button(
            onClick = { onColorSelected(color) },
            colors = ButtonDefaults.buttonColors(backgroundColor = color),
            shape = CircleShape,
            modifier = Modifier.size(48.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = textColor
                )
            }
        }
    }
}