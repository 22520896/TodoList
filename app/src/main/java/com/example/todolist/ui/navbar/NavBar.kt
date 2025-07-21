package com.example.todolist.ui.navbar

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.todolist.ui.ChooseAdd
import com.example.todolist.ui.event.EventModal
import com.example.todolist.ui.todo.TodoModal
import com.example.todolist.ui.todo.TodoModalViewModel
import com.example.todolist.viewmodel.CommonViewModel
import com.example.todolist.viewmodel.EventModalViewModel
import com.example.todolist.viewmodel.NavBarViewModel
import kotlinx.coroutines.launch

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBar(
    navController: NavController,
    viewModel: NavBarViewModel = hiltViewModel(),
    commonViewModel: CommonViewModel,
    content: @Composable (PaddingValues) -> Unit,
) {
    val leftItems = listOf(NavDes.TODO, NavDes.CALENDAR)
    val rightItems = listOf(NavDes.REPORT, NavDes.SETTING)

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    var currentModal by remember { mutableStateOf<NavBarViewModel.UiEvent?>(null) }
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val todoModalViewModel: TodoModalViewModel = hiltViewModel()
    val eventModalViewModel: EventModalViewModel = hiltViewModel()
    val color by commonViewModel.color.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.event.collect { e ->
            when (e) {
                is NavBarViewModel.UiEvent.NavigateToNewNote -> {
                    navController.navigate(NavDes.NOTE_EDIT.route)
                    {
//                        popUpTo(navController.graph.findStartDestination().id) {
//                            saveState = true
//                        }
//                        launchSingleTop = true
//                        restoreState = true
                        popUpTo(NavDes.NOTE.route) { inclusive = false }
                        launchSingleTop = true
                        restoreState = true
                    }
                }

                else -> {
                    currentModal = e
                    scope.launch { sheetState.show() }
                }
            }
        }
    }

    if (currentModal != null) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                    currentModal = null
                }
            },
            sheetState = sheetState,
            dragHandle       = {}
        ) {
            when (currentModal) {
                NavBarViewModel.UiEvent.ShowAddTodoModal  -> {
                    todoModalViewModel.startAddTodo(commonViewModel.initDate.value)
                    TodoModal(viewModel = todoModalViewModel, commonViewModel = commonViewModel, onDismiss = { currentModal = null })
                }
                NavBarViewModel.UiEvent.ShowAddEventModal -> {
                    eventModalViewModel.startAddEvent()
                    EventModal(eventModalViewModel, commonViewModel, { currentModal = null })
                }
                NavBarViewModel.UiEvent.ShowChooseModal   -> {
                    todoModalViewModel.startAddTodo(commonViewModel.initDate.value)
                    TodoModal(viewModel = todoModalViewModel, commonViewModel = commonViewModel, onDismiss = { currentModal = null })
                }
                else -> {}    // NavigateToNewNote không rơi vào đây
            }
        }
    }

    val bgColor = Color(android.graphics.Color.parseColor(color))
    Scaffold(
        Modifier.safeDrawingPadding(),
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onFabClick(currentRoute) },
                backgroundColor = bgColor,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(72.dp),
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 0.dp
                ),
            )
                {
                Icon(
                    painter = painterResource(id = NavDes.ADD.icon),
                    contentDescription = NavDes.ADD.label,
                    modifier = Modifier.size(28.dp)
                )
            }
        },
        bottomBar = {
            BottomAppBar(
                cutoutShape = CircleShape,
                backgroundColor = MaterialTheme.colors.surface,
                elevation = 10.dp,
                modifier = Modifier
            ) {
                leftItems.forEach { item ->
                    BottomNavigationItem(
                        icon = { Icon(painterResource(item.icon), contentDescription = null, modifier = Modifier.size(25.dp)) },
                        label = {
                            Text(
                                item.label,
                                style = TextStyle(fontSize = 11.sp)
                            )
                        },
                        selected = currentRoute == (item.route) || (currentRoute?.endsWith("note") == true && item.route == NavDes.TODO.route),
                        selectedContentColor = bgColor,
                        unselectedContentColor = Color.Gray,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                }

                Spacer(Modifier.weight(1f, true))

                rightItems.forEach { item ->
                    BottomNavigationItem(
                        icon = { Icon(painterResource(item.icon), contentDescription = null, modifier = Modifier.size(25.dp)) },
                        label = {
                            Text(
                                item.label,
                                style = TextStyle(fontSize = 11.sp),
                            )
                        },
                        selected = currentRoute == item.route,
                        selectedContentColor = bgColor,
                        unselectedContentColor = Color.Gray,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                }
            }
        },
        content = content
    )
}



