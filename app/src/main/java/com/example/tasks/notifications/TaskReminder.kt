package com.example.tasks.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.tasks.MainActivity
import com.example.tasks.R

class TaskReminder : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("Tasks Notification","Notification builder was triggered")
        val appIntent = Intent(context, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(context, "Task reminders")
            .setSmallIcon(R.drawable.ic_baseline_library_add_check_24)
            .setContentTitle(intent.getStringExtra("Task name"))
            .setContentText(intent.getStringExtra("Task description")) //TODO add some meaningful description
            .setContentIntent(pendingIntent)
            .setCategory(NotificationCompat.CATEGORY_REMINDER)
            .setAutoCancel(true)
            .build()

        val manager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(intent.getLongExtra("Task ID",0L).toInt(),notification)
    }
}