package com.example.tasks.ui

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.tasks.R
import com.example.tasks.Screen

@Composable
fun CustomTopAppBar(screenName: String) {
    TopAppBar(
        title = {
            Text(screenName)
        },
        actions = {
            var expanded by remember { mutableStateOf(false) }

            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_more_vert_24),
                    contentDescription = "Dropdown menu"
                )
            }

            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(onClick = {
                    expanded = false
                    //viewModel.setPriorityCategory()
                }) {
                    Text(stringResource(id = R.string.sort_by_priority))
                }
                DropdownMenuItem(onClick = {
                    expanded = false
                    //viewModel.setDeadlineCategory()
                }) {
                    Text(stringResource(id = R.string.sort_by_deadline))
                }
                DropdownMenuItem(onClick = {
                    expanded = false
                    //viewModel.toggleCompleted()
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
fun CustomBottomNavigation() {
    val items = listOf(Screen.TaskList, Screen.Calendar, Screen.Overview)
    BottomNavigation {
        items.forEach { screen ->
            BottomNavigationItem(
                selected = false,
                onClick = {},
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
