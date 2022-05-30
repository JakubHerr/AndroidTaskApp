package com.example.tasks.ui

import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.tasks.R
import com.example.tasks.Screen

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
