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

class Notification : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("Tasks Notification","Notification builder was triggered")
        val appIntent = Intent(context, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_IMMUTABLE)

        //TODO replace placeholder text
        val notification = NotificationCompat.Builder(context, "channel id")
            .setSmallIcon(R.drawable.ic_baseline_library_add_check_24)
            .setContentTitle("Task app")
            .setContentText("You have 2 deadlines today")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        val manager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1,notification)
    }
}