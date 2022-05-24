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
import com.example.tasks.R
import com.example.tasks.ui.theme.TasksTheme

@Composable
fun TaskList(onClick: () -> Unit) {
    val categories = listOf("Overdue", "Future", "No deadline")
    LazyColumn {
        items(categories.size) {
            Category(categories[it], modifier = Modifier.padding(8.dp))
        }
    }
    FloatingActionButton(onClick = {onClick()}) {
        Icon(painter = painterResource(id = R.drawable.ic_baseline_add_24), contentDescription = "Add Task FAB")
    }
}

@Composable
fun Category(name: String, modifier: Modifier = Modifier) {
    var isExpanded by remember { mutableStateOf(true) }

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
                Text(name)
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
                    TaskItem()
                    TaskItem()
                    TaskItem()
                }
            }
        }

    }
}

@Composable
fun TaskItem() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = true, onCheckedChange = {})
            Text(text = "Placeholder")
        }
        Text(text = "July 20 1969", modifier = Modifier.padding(8.dp))
    }
}