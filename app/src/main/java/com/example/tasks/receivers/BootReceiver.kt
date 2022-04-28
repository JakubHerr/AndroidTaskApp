package com.example.tasks.receivers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.tasks.notifications.Notification
import java.util.*

class BootReceiver : BroadcastReceiver() {
    //after reboot, a test notification with placeholder text is scheduled
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("Tasks BootReceiver","some broadcast was received")
        if (intent.action == "android.intent.action.LOCKED_BOOT_COMPLETED") {
            Log.d("Tasks BootReceiver","${intent.action} was received")

            val time = Calendar.getInstance()

            val intentNotification = Intent(context, Notification::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context.applicationContext,
                1,
                intentNotification,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            time.add(Calendar.MINUTE,1)
            Log.d("Tasks BootReceiver","exact alarm set for $time")
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,time.timeInMillis,pendingIntent)
        }
    }

}