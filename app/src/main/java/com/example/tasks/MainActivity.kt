package com.example.tasks

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.tasks.notifications.TaskReminder
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.NavHostFragment) as NavHostFragment
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNav.setupWithNavController(navHostFragment.navController)

        //hiding bottom navigation prevents escaping from unsaved tasks
        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            bottomNav.visibility = when (destination.id) {
                R.id.addTaskFragment, R.id.editTaskFragment  -> View.GONE
                else -> View.VISIBLE
            }
        }

        createNotificationChannel()
    }

    private fun scheduleNotification() {
        val intent = Intent(applicationContext, TaskReminder::class.java)
        val pendingIntent = PendingIntent.getBroadcast(applicationContext,
            1,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = Calendar.getInstance()
        time.add(Calendar.MINUTE,1)

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,time.timeInMillis,pendingIntent)
        Toast.makeText(this,"Notification set to one minute from now",Toast.LENGTH_LONG).show()
    }

    private fun createNotificationChannel() {
        //minimum SDK is >= 26, no need to check
        val name = getString(R.string.channel_name)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("Task reminders", name, importance).apply {
            description = getString(R.string.channel_description)
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}