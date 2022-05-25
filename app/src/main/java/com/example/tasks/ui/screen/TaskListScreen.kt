package com.example.tasks.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tasks.R
import com.example.tasks.data.Category
import com.example.tasks.ui.theme.TasksTheme
import com.example.tasks.ui.theme.viewmodel.TaskViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@Composable
fun TaskList() {
    val viewModel = hiltViewModel<TaskViewModel>()
    val categories = viewModel.categories
    LazyColumn {
        items(categories.size) {
            Category(categories[it], modifier = Modifier.padding(8.dp))
        }
    }
}

@Composable
fun Category(category: Category, modifier: Modifier = Modifier) {
    var isExpanded by remember { mutableStateOf(true) }
    val viewModel = hiltViewModel<TaskViewModel>()
    val taskList = viewModel.test.collectAsState()

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
                Column{
                    taskList.value.forEach {
                        TaskItem(name = it.taskName, deadline = "Placeholder")
                    }
                }
            }
        }
    }
}

@Composable
fun TaskItem(name: String, deadline: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = true, onCheckedChange = {})
            Text(text = name)
        }
        Text(text = deadline, modifier = Modifier.padding(8.dp))
    }
}