package com.example.tasks.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.chargemap.compose.numberpicker.NumberPicker
import com.example.tasks.R
import com.example.tasks.Screen
import com.example.tasks.data.Task
import com.example.tasks.extensions.format
import com.example.tasks.ui.screen.TaskAddScreenEvent
import com.example.tasks.ui.screen.TaskPriority

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

@Composable
fun TaskNameField(name: String, onValueChange: (String) -> Unit) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        value = name,
        onValueChange = { onValueChange(it) },
        label = { Text(stringResource(id = R.string.task_name_prompt)) })
}

@Composable
fun DeadlineButton(task: Task, onEvent: (TaskAddScreenEvent) -> Unit) {
    Button(onClick = { onEvent(TaskAddScreenEvent.SetDeadline(task)) }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_calendar_month_24),
            contentDescription = stringResource(
                id = R.string.time_picker_description
            )
        )
        Text(task.deadline?.format() ?: stringResource(id = R.string.no_date_selected))
    }
}

@Composable
fun PriorityDropdownMenu(expanded: Boolean, onSelect: (TaskPriority) -> Unit) {
    DropdownMenu(expanded = expanded, onDismissRequest = { onSelect(TaskPriority.NO) }) {
        DropdownMenuItem(onClick = { onSelect(TaskPriority.HIGH) }) {
            Text("High")
        }
        DropdownMenuItem(onClick = { onSelect(TaskPriority.MEDIUM) }) {
            Text("Medium")
        }
        DropdownMenuItem(onClick = { onSelect(TaskPriority.LOW) }) {
            Text("Low")
        }
        DropdownMenuItem(onClick = { onSelect(TaskPriority.NO) }) {
            Text("No")
        }
    }
}

//TODO make prettier
@Composable
fun EstimatePicker(onDismiss: () -> Unit, onCancel: () -> Unit, onSet: (Int) -> Unit) {
    var hours by remember { mutableStateOf(0) }
    var minutes by remember { mutableStateOf(0) }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NumberPicker(
                        value = hours,
                        range = 0..99,
                        onValueChange = {
                            hours = it
                        }
                    )
                    Text(text = "Hours", modifier = Modifier.padding(8.dp, 0.dp, 24.dp, 0.dp))
                    NumberPicker(
                        value = minutes,
                        range = 0..99,
                        onValueChange = {
                            minutes = it
                        }
                    )
                    Text(text = "Minutes", modifier = Modifier.padding(8.dp, 0.dp, 24.dp, 0.dp))
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Button(modifier = Modifier.padding(8.dp), onClick = { onCancel() }) {
                        Text("Cancel")
                    }
                    Button(
                        modifier = Modifier.padding(8.dp),
                        onClick = { onSet(hours * 60 + minutes) }) {
                        Text("OK")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun EstimatePickerPreview() {
    EstimatePicker(onDismiss = {}, onCancel = {}, onSet = {})
}