package com.example.tasks.receivers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.tasks.data.TaskDao
import com.example.tasks.extensions.alarmManager
import com.example.tasks.notifications.TaskReminder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {
    @Inject
    lateinit var dao: TaskDao

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.LOCKED_BOOT_COMPLETED") {
            Log.d("Tasks BootReceiver","${intent.action} was received")
            rescheduleAlarms(context)
        }
    }

    private fun rescheduleAlarms(context: Context) = runBlocking {
        launch {
            Log.d("Tasks BootReceiver","coroutine started successfully")
            val alarmManager = context.alarmManager()

            val taskList = dao.getAllFuture(Calendar.getInstance().timeInMillis)
            taskList.forEach { task ->
                //create a pending intent for each task reminder
                val intentNotification = Intent(context, TaskReminder::class.java)
                    .putExtra("Task ID",task.taskId)
                    .putExtra("Task name",task.taskName)
                    .putExtra("Task description","placeholder text")

                val pendingIntent = PendingIntent.getBroadcast(context.applicationContext,
                    task.taskId.toInt(),
                    intentNotification,
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,task.deadline.timeInMillis,pendingIntent)
                Log.d("Tasks BootReceiver","exact alarm set for task nr. ${task.taskId} at ${task.deadline}")
            }
            if (taskList.isEmpty()) Log.d("Tasks BootReceiver","List of tasks was empty")
        }
    }

}