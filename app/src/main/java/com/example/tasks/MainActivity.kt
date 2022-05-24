package com.example.tasks

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.animateContentSize
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tasks.notifications.TaskReminder
import com.example.tasks.ui.theme.TasksTheme
import com.example.tasks.ui.theme.viewmodel.TaskViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: TaskViewModel by viewModels()
        setContent {
            TasksTheme {
                Navigation()
            }
        }
    }

    private fun scheduleNotification() {
        val intent = Intent(applicationContext, TaskReminder::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            1,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = Calendar.getInstance()
        time.add(Calendar.MINUTE, 1)

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time.timeInMillis,
            pendingIntent
        )
        Toast.makeText(this, "Notification set to one minute from now", Toast.LENGTH_LONG).show()
    }

    //minimum SDK is >= 26, no need to check
    private fun createNotificationChannel(name: String, description: String) {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("Task reminders", name, importance).apply {
            this.description = description
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

@Preview(showBackground = true)
@Composable
fun Navigation() {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()

    Scaffold(scaffoldState = scaffoldState, topBar = { CustomTopAppBar() }, bottomBar = { CustomBottomNavigation(navController)}) {
        NavHost(navController = navController, startDestination = Screen.TaskList.route) {
            composable(Screen.TaskList.route) {
                TaskList()
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

@Preview
@Composable
fun TaskList() {
    Category()
}

@Composable
fun Category() {
    var isExpanded by remember { mutableStateOf(true) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .animateContentSize(), elevation = 8.dp, color = Color.Gray
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
                Text("Category name")
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
                LazyColumn {
                    items(3) {
                        TaskItem()
                    }
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

@Composable
fun CustomTopAppBar() {
    TopAppBar(
        title = {
            Text(text = "Placeholder")
        },
        actions = {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_schedule_24),
                contentDescription = "Icon"
            )
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
fun CustomBottomNavigation(navController: NavHostController) {
    BottomNavigation() {
        BottomNavigationItem(
            selected = navController.currentBackStackEntry?.id == Screen.TaskList.route,
            onClick = { navController.navigate(Screen.TaskList.route) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_library_add_check_24),
                    contentDescription = "Task list"
                )
            })
        BottomNavigationItem(
            selected = false,
            onClick = { navController.navigate(Screen.Calendar.route) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_calendar_month_24),
                    contentDescription = "Calendar"
                )
            })
        BottomNavigationItem(
            selected = false,
            onClick = { navController.navigate(Screen.Overview.route) },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_format_list_numbered_24),
                    contentDescription = "statistics"
                )
            })
    }
}
