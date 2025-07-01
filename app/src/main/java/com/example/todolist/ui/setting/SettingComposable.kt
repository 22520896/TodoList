package com.example.todolist.ui.setting

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.todolist.utils.getRingtones
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    value: String?,
    isColor: Boolean = false,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(end = 0.dp)) {
            if (isColor) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(Color(android.graphics.Color.parseColor(value ?: "#FFFFFF")), CircleShape)
                        .border(1.dp, Color.Gray, CircleShape)
                )
            }
            else {
                Text(
                    text = value ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    maxLines = 1
                )
            }

            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Chọn"
                )
            }
        }
    }
}



@Composable
fun DateFormatDialog(
    currentFormat: String,
    onFormatSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val today = LocalDate.now()
    val formats = listOf(
        "dd/MM/yyyy" to today.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
        "MM/dd/yyyy" to today.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")),
        "yyyy/MM/dd" to today.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")),
        "yyyy/dd/MM" to today.format(DateTimeFormatter.ofPattern("yyyy/dd/MM"))
    )
    var selectedFormat by remember { mutableStateOf(currentFormat) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Chọn định dạng ngày") },
        text = {
            Column {
                formats.forEach { (format, example) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedFormat = format
                                onFormatSelected(format)
                                onDismiss()
                            }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedFormat == format,
                            onClick = {
                                selectedFormat = format
                                onFormatSelected(format)
                                onDismiss()
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("$format ($example)")
                    }
                }
            }
        },
        confirmButton = {}
    )
}

@Composable
fun TimeFormatDialog(
    currentFormat: String,
    onFormatSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedFormat by remember { mutableStateOf(currentFormat) }

    val formats = listOf(
        "12 giờ" to "hh:mm a",
        "24 giờ" to "HH:mm"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Chọn định dạng thời gian") },
        text = {
            Column {
                formats.forEach { (pattern, format) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selectedFormat = format
                                onFormatSelected(format)
                                onDismiss()
                            }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedFormat == format,
                            onClick = {
                                selectedFormat = format
                                onFormatSelected(format)
                                onDismiss()
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(pattern)
                    }
                }
            }
        },
        confirmButton = {}
    )
}

@Composable
fun ColorPickerDialog(
    selectedColor: String,
    onColorSelected: (String) -> Unit,
    onDismissRequest: () -> Unit
) {
    val colors = listOf(
        "#4CD4A0", "#A18EFF", "#5C9EFF", "#FFA07A",  "#FF69B4", "#4CAF81"
    )

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {},
        title = { Text("Chọn màu nền") },
        text = {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                colors.forEach { color ->
                    val isSelected = color.equals(selectedColor, ignoreCase = true)
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .padding(4.dp)
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) Color.LightGray else Color.Gray,
                                shape = CircleShape
                            )
                            .background(
                                color = Color(android.graphics.Color.parseColor(color)),
                                shape = CircleShape
                            )
                            .clickable { onColorSelected(color) }
                    )
                }
            }
        }
    )
}

@Composable
fun RingtonePickerDialog(
    context: Context,
    selectedUri: String,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {

    val ringtones = remember { getRingtones(context) }
    var selected by remember { mutableStateOf(selectedUri) }
    var currentRingtone by remember { mutableStateOf<Ringtone?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Chọn nhạc chuông") },
        text = {
            LazyColumn(modifier = Modifier.height(300.dp)) {
                items(ringtones) { (title, uri) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                selected = uri
                                currentRingtone?.stop()
                                val ringtone = RingtoneManager.getRingtone(context, Uri.parse(uri))
                                if (ringtone != null) {
                                    currentRingtone = ringtone
                                    ringtone.play()
                                }
                            }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selected == uri,
                            onClick = {
                                selected = uri
                                currentRingtone?.stop()
                                val ringtone = RingtoneManager.getRingtone(context, Uri.parse(uri))
                                if (ringtone != null) {
                                    currentRingtone = ringtone
                                    ringtone.play()
                                }
                            }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = title)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                currentRingtone?.stop()
                val chosen = ringtones.find { it.second == selected }
                if (chosen != null) {
                    onConfirm(chosen.second, chosen.first)
                }
                onDismiss()
            }) {
                Text("Hoàn tất")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                currentRingtone?.stop()
                onDismiss()
            }) {
                Text("Hủy bỏ")
            }
        }
    )

    DisposableEffect(Unit) {
        onDispose {
            currentRingtone?.stop()
            currentRingtone = null
        }
    }
}

