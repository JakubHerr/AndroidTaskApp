package com.example.tasks

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import java.util.*

class BootReceiver : BroadcastReceiver() {
    //set an alarm one minutes after receiving boot_completed for testing purposes
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("BootReceiver","some broadcast was received")
        if (intent.action == "android.intent.action.QUICKBOOT_POWERON" ||
            intent.action == "android.intent.action.BOOT_COMPLETED") {
            Log.d("BootReceiver","${intent.action} was received")
            val intentNotification = Intent(context, Notification::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context.applicationContext,
                1,
                intentNotification,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            val time = Calendar.getInstance()
            time.add(Calendar.MINUTE,1)
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,time.timeInMillis,pendingIntent)
            Log.d("BootReceiver","Exact alarm was set to $time")
        }
    }
}