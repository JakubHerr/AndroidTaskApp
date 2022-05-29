package com.example.tasks.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tasks.R
import com.example.tasks.Screen
import com.example.tasks.ui.screen.TaskAddScreen
import com.example.tasks.ui.screen.TaskEditScreen
import com.example.tasks.ui.screen.TaskList
import com.example.tasks.ui.theme.viewmodel.TaskViewModel

@Preview(showBackground = true)
@Composable
fun Navigation() {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { CustomTopAppBar(currentDestination) },
        bottomBar = { CustomBottomNavigation(navController, currentDestination) },
        backgroundColor = MaterialTheme.colors.background,
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            when (currentDestination?.route) {
                Screen.TaskList.route -> AddTaskFab(navController = navController)
            }
        }
    ) {
        NavHost(navController = navController, startDestination = Screen.TaskList.route, modifier = Modifier.padding(it)) {
            composable(Screen.TaskList.route) {
                TaskList()
            }
            composable(route = Screen.AddTask.route) {
                TaskAddScreen(navController)
            }
            composable(route = "${Screen.EditTask.route}/taskId",
                arguments = listOf(navArgument("taskId") {
                    type = NavType.LongType
                }
                )) {
                TaskEditScreen(navController, it.arguments?.getLong("taskId"))
            }


            composable(Screen.Calendar.route) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(text = "Calendar", modifier = Modifier.align(Alignment.Center))
                }
            }
            composable(Screen.Overview.route) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(text = "Overview", modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}

@Composable
fun CustomTopAppBar(currentDestination: NavDestination?) {
    val viewModel = hiltViewModel<TaskViewModel>()

    TopAppBar(
        title = {
            Text(text = currentDestination?.route ?: "Placeholder")
        },
        actions = {
            var expanded by remember { mutableStateOf(false) }
            
            IconButton(onClick = {expanded = !expanded}) {
                Icon(painter = painterResource(id = R.drawable.ic_baseline_more_vert_24), contentDescription = "Dropdown menu")
            }
            
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(onClick = {
                    expanded = false
                    viewModel.setPriorityCategory()}) {
                    Text(stringResource(id = R.string.sort_by_priority))
                }
                DropdownMenuItem(onClick = {
                    expanded = false
                    viewModel.setDeadlineCategory()
                }) {
                    Text(stringResource(id = R.string.sort_by_deadline))
                }
                DropdownMenuItem(onClick = {
                    expanded = false
                    viewModel.toggleCompleted()
                    }) {
                    Text(stringResource(id = R.string.show_completed))
                }
            }
        },
        navigationIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_menu_24),
                contentDescription = "Placeholder"
            )
        }
    )
}

@Composable
fun CustomBottomNavigation(navController: NavHostController, currentDestination: NavDestination?) {
    val items = listOf(Screen.TaskList, Screen.Calendar, Screen.Overview)
    when (currentDestination?.route) {
        Screen.AddTask.route -> {}
        Screen.EditTask.route -> {}
        else -> BottomNavigation {
            items.forEach { screen ->
                BottomNavigationItem(
                    selected = currentDestination?.hierarchy?.any { it.route == currentDestination.route } == true,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    icon = {
                        screen.iconId?.let { icon ->
                            Icon(
                                painter = painterResource(id = icon),
                                contentDescription = stringResource(id = screen.resourceId)
                            )
                        }
                    },
                    label = { Text(stringResource(screen.resourceId)) },
                )
            }
        }

    }
}

@Composable
fun AddTaskFab(navController: NavHostController) {
    FloatingActionButton(onClick = {
        Log.d("Navigation", "AddTask FAB pressed")
        navController.navigate(Screen.AddTask.route)
    }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_add_24),
            contentDescription = "Add Task FAB"
        )
    }
}

@Composable
fun EditTaskFab() {

}

@Composable
fun SaveTaskFab() {

}
