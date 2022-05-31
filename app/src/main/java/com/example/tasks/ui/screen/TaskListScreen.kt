package com.example.tasks.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tasks.R
import com.example.tasks.data.Category
import com.example.tasks.data.Task
import kotlinx.coroutines.flow.MutableStateFlow

sealed class TaskListEvent {
    object SortByDeadline : TaskListEvent()
    object SortByPriority : TaskListEvent()
    object ShowCompleted : TaskListEvent()
    object AddTask : TaskListEvent()
    data class TaskCompleted(val id: Long) : TaskListEvent()
}

@Composable
fun TaskListScreen(categories: MutableStateFlow<List<Category>>, onEvent: (TaskListEvent) -> Unit) {
    val scaffoldState = rememberScaffoldState()
    val categoryList = categories.collectAsState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { CustomTopAppBar(screenName = stringResource(id = R.string.task_list), onEvent) },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            AddTaskFab { onEvent(TaskListEvent.AddTask) }
        }
    ) {
        LazyColumn {
            items(categoryList.value.size) { idx ->
                //Categories get don't get created but recomposed/recycled on sorting change
                //this results in isExpanded being remembered when it should not
                //TODO try to fix
                Category(
                    category = categoryList.value[idx],
                    onEvent = onEvent,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun Category(category: Category, onEvent: (TaskListEvent) -> Unit, modifier: Modifier = Modifier) {
    var isExpanded by remember { mutableStateOf(true) }
    val taskList = category.tasks.collectAsState(initial = emptyList())

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .animateContentSize(),
        elevation = 16.dp,
        color = MaterialTheme.colors.primary
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(category.name)
                IconToggleButton(
                    checked = isExpanded,
                    onCheckedChange = { isExpanded = !isExpanded }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_keyboard_arrow_left_24),
                        contentDescription = "expand list"
                    )
                }
            }
            Spacer(modifier = Modifier.size(2.dp))
            if (isExpanded) {
                Column {
                    taskList.value.forEach {
                        TaskItem(it, onEvent)
                    }
                }
            }
        }
    }
}

@Composable
fun TaskItem(task: Task, onEvent: (TaskListEvent) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = task.taskDone,
                onCheckedChange = {
                    onEvent(TaskListEvent.TaskCompleted(task.taskId))
                })
            Text(task.taskName)
        }
        Text(text = task.deadline?.toString() ?: "No deadline", modifier = Modifier.padding(8.dp))
    }
}

@Composable
fun AddTaskFab(onClick: () -> Unit) {
    FloatingActionButton(onClick = { onClick() }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_add_24),
            contentDescription = "Add Task FAB"
        )
    }
}

@Composable
fun CustomTopAppBar(screenName: String, onEvent: (TaskListEvent) -> Unit) {
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
                    onEvent(TaskListEvent.SortByPriority)
                }) {
                    Text(stringResource(id = R.string.sort_by_priority))
                }
                DropdownMenuItem(onClick = {
                    expanded = false
                    onEvent(TaskListEvent.SortByDeadline)
                }) {
                    Text(stringResource(id = R.string.sort_by_deadline))
                }
                DropdownMenuItem(onClick = {
                    expanded = false
                    onEvent(TaskListEvent.ShowCompleted)
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