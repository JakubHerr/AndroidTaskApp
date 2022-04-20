package com.example.tasks

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class Notification : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notification = NotificationCompat.Builder(context, "channel id")
            .setSmallIcon(R.drawable.ic_baseline_library_add_check_24)
            .setContentTitle("Task app")
            .setContentText("You have 2 deadlines today")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val manager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1,notification)
    }
}