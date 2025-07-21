package com.example.todolist.ui.home.note

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.wear.compose.material.Text
import com.example.todolist.viewmodel.NoteViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.todolist.viewmodel.CommonViewModel


//@Composable
//fun NoteEditScreen(navController: NavController, viewModel: NoteViewModel, id: Long){
//    Text("Edit")
//}


//Danh sách các icon ở thanh Bar
enum class NoteIcon { Bold, Image, Bullet, Color, Highlight}

// Dữ liệu cho từng icon
data class NoteIconItem(val type: NoteIcon, val icon: ImageVector, val description: String)

// Danh sách các icon
val noteIcons: List<NoteIconItem>
    get() = listOf(
        NoteIconItem(NoteIcon.Bold, Icons.Default.FormatBold, "Bold"),
        NoteIconItem(NoteIcon.Image, Icons.Default.Image, "Image"),
        NoteIconItem(NoteIcon.Bullet, Icons.Default.FormatListBulleted, "Bullet"),
        NoteIconItem(NoteIcon.Color, Icons.Default.ColorLens, "Color"),
        NoteIconItem(NoteIcon.Highlight, Icons.Default.Highlight, "Highlight"),
    )

@Composable
fun NoteEditScreen(
    navController: NavController,
    viewModel: NoteViewModel,
    commonViewModel: CommonViewModel,
    noteId: Long?,
    modifier: Modifier
) {
    val color by commonViewModel.color.collectAsStateWithLifecycle()
    val notes by viewModel.allNotes.collectAsState(initial = emptyList())
    val noteToEdit = notes.find { it.id == noteId }

    val title by viewModel.title.collectAsState()
    val content by viewModel.content.collectAsState()
    var selectedIcon by remember { mutableStateOf<NoteIcon?>(null) }
    var showErrorDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(noteToEdit) {
        if (noteToEdit != null) {
            viewModel.startEditNote(noteToEdit)
        } else {
            viewModel.startAddNote()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(android.graphics.Color.parseColor(color))).then(modifier),
        containerColor = Color.Transparent,
        topBar = {
            NoteEditTopBar(
                onCancel = { navController.popBackStack() },
                onSave = {
                    if (title.isNotBlank() || content.isNotEmpty()) {
                        viewModel.saveNote()
                        navController.popBackStack()
                    }
                    else{
                        showErrorDialog = true
                    }

                }
            )
        }
    ) { padding ->
        NoteEditContent(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .imePadding(),
            title = title,
            content = content,
            selectedIcon = selectedIcon,
            onTitleChange = viewModel::updateTitle,
            onContentChange = viewModel::updateContent,
            onIconClick = {
                selectedIcon = if (selectedIcon == it) null else it
            }
        )
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    androidx.compose.material3.Text("OK")
                }
            },
            title = { Text("Lỗi") },
            text = { Text("Không được để trống cả Tiêu đề và Chi tiết", color = Color.Black) }
        )
    }
}

@Composable
fun NoteEditTopBar(
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .statusBarsPadding(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onCancel) {
            Icon(Icons.Default.Close, contentDescription = "Cancel")
        }
        Text(text = "Ghi chú",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold), color = Color.Black)
        IconButton (onClick = onSave) {
            Icon(Icons.Default.Check, contentDescription = "Save")
        }
    }
}

@Composable
fun NoteEditContent(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    selectedIcon: NoteIcon?,
    onTitleChange: (String) -> Unit,
    onContentChange: (String) -> Unit,
    onIconClick: (NoteIcon) -> Unit
) {
    Column(modifier = modifier) {
        NoteTitleField(title, onTitleChange)
        Spacer(modifier = Modifier.height(8.dp))
        NoteContentField(
            value = content,
            onChange = onContentChange,
            modifier = Modifier.weight(1f)
        )
//        NoteToolbar(selectedIcon, onIconClick)
    }
}

@Composable
fun NoteTitleField(value: String, onChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onChange,
        placeholder = {
            Text(
                "Tiêu đề",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.Gray
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(18.dp))
            .background(Color.White, RoundedCornerShape(16.dp)), // Nền trắng
        shape = RoundedCornerShape(12.dp),
        textStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 22.sp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedTextColor = Color.Black
        )
    )
}

@Composable
fun NoteContentField(
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onChange,
        placeholder = { Text("Chi tiết", color = Color.Gray) },
        modifier = modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(18.dp)) // Bóng nhẹ giống TodoItemCard
            .background(Color.White, RoundedCornerShape(16.dp)), // Nền trắng
        maxLines = Int.MAX_VALUE,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedContainerColor = Color.White, // Nền trắng khi focus
            unfocusedContainerColor = Color.White, // Nền trắng khi không focus
            focusedTextColor = Color.Black
        )
    )
}

@Composable
fun NoteToolbar(
    selectedIcon: NoteIcon?,
    onIconClick: (NoteIcon) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(3.dp, RoundedCornerShape(18.dp)) // Bóng nhẹ giống TodoItemCard
            .background(Color.White, RoundedCornerShape(16.dp)) // Nền trắng
            .padding(8.dp), // Thêm padding để nội dung không sát mép
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        noteIcons.forEach { item ->
            IconButton(
                onClick = { onIconClick(item.type) },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = if (selectedIcon == item.type)
                        Color(0xFF388E3C) else LocalContentColor.current
                )
            ) {
                Icon(item.icon, item.description)
            }
        }
    }
}