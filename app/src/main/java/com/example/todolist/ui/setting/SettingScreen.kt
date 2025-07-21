package com.example.todolist.ui.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.todolist.viewmodel.CommonViewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todolist.R
import kotlinx.coroutines.delay

@Composable
fun SettingScreen(
    viewModel: CommonViewModel = hiltViewModel()
) {
    val dateFormat by viewModel.dateFormat.collectAsStateWithLifecycle()
    val timeFormat by viewModel.timeFormat.collectAsStateWithLifecycle()
    val color by viewModel.color.collectAsStateWithLifecycle()
    val ringtoneName by viewModel.ringtoneName.collectAsStateWithLifecycle()
    val ringtoneUri by viewModel.ringtoneUri.collectAsStateWithLifecycle()

    var showDateFormatDialog by rememberSaveable { mutableStateOf(false) }
    var showTimeFormatDialog by rememberSaveable { mutableStateOf(false) }
    var showColorDialog by rememberSaveable { mutableStateOf(false) }
    var showRingtoneDialog by rememberSaveable { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Cài đặt",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
        )

//        // Ảnh
//        Image(
//            painter = painterResource(id = R.drawable.cat),
//            contentDescription = null,
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(160.dp),
//            contentScale = ContentScale.Crop
//        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.Transparent)
                .padding(horizontal = 10.dp, vertical = 4.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.cat), // thay bằng ảnh bạn cung cấp
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(24.dp))
            )
        }

        if (dateFormat == "") {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Loading...")
            }
            return
        }

        // Danh sách cài đặt
        Box(
            modifier = Modifier
                .wrapContentSize() // Chiếm đúng kích cỡ nội dung
                .padding(horizontal = 10.dp, vertical = 6.dp)
                .fillMaxWidth()
                .shadow(3.dp, RoundedCornerShape(18.dp)) // Bóng nhẹ giống TodoItemCard
                .background(Color.White, RoundedCornerShape(16.dp)) // Nền trắng
                .padding(6.dp)
        ){
            Column(
                modifier = Modifier.fillMaxWidth(), // Column chiếm toàn chiều rộng để căn chỉnh
                verticalArrangement = Arrangement.spacedBy(2.dp) // Khoảng cách giữa các mục
            ) {
                    SettingItem(
                        icon = Icons.Default.CalendarMonth,
                        title = "Định dạng ngày",
                        value = dateFormat,
                        onClick = {
                            showDateFormatDialog = true
                        }
                    )

                Divider(
                    color = Color.Gray.copy(alpha = 0.2f),
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )

                    SettingItem(
                        icon = Icons.Default.AccessTime,
                        title = "Định dạng giờ",
                        value = if (timeFormat == "HH:mm") "24 giờ" else "12 giờ",
                        onClick = {
                            showTimeFormatDialog = true
                        }
                    )
                Divider(
                    color = Color.Gray.copy(alpha = 0.2f),
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )

                    SettingItem(
                        icon = Icons.Default.ColorLens,
                        title = "Màu nền",
                        value = color,
                        isColor = true,
                        onClick = {
                            showColorDialog = true
                        }
                    )

                Divider(
                    color = Color.Gray.copy(alpha = 0.2f),
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )

                    SettingItem(
                        icon = Icons.Default.MusicNote,
                        title = "Nhạc chuông",
                        value = ringtoneName,
                        onClick = {
                            showRingtoneDialog = true
                        },
                    )
            }
        }

        if (showDateFormatDialog) {
            DateFormatDialog(
                currentFormat = dateFormat,
                onFormatSelected = { newFormat ->
                    viewModel.updateDateFormat(newFormat)
                    showDateFormatDialog = false
                },
                onDismiss = { showDateFormatDialog = false }
            )
        }

        if (showTimeFormatDialog) {
            TimeFormatDialog(
                currentFormat = timeFormat,
                onFormatSelected = { newFormat ->
                    viewModel.updateTimeFormat(newFormat)
                    showTimeFormatDialog = false
                },
                onDismiss = { showTimeFormatDialog = false }
            )
        }

        if (showColorDialog) {
            ColorPickerDialog(
                selectedColor = color,
                onColorSelected = {
                    viewModel.updateColor(it)
                    showColorDialog = false
                },
                onDismissRequest = { showColorDialog = false }
            )
        }

        val context = LocalContext.current
        if (showRingtoneDialog) {
            RingtonePickerDialog(
                context = context,
                selectedUri = ringtoneUri,
                onDismiss = { showRingtoneDialog = false },
                onConfirm = { uri, name ->
                    viewModel.updateRingtone(uri, name)
                    showRingtoneDialog = false
                }
            )
        }
    }
}



