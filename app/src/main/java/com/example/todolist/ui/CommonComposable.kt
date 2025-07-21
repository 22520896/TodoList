package com.example.todolist.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun CommonSwitch(color: String, isCheck: Boolean, onCheckedChange: (Boolean) -> Unit){
    Switch(checked = isCheck,
        onCheckedChange = onCheckedChange,
        modifier = Modifier.scale(0.7f),
        colors = SwitchDefaults.colors(
            checkedThumbColor = Color.White,
            checkedTrackColor = Color(android.graphics.Color.parseColor(color)),
            uncheckedTrackColor = Color.LightGray
        ))
}


@Composable
fun CommonTitleField (title: String, modifier: Modifier = Modifier){
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        fontSize = 18.sp,
        modifier = modifier
    )
}