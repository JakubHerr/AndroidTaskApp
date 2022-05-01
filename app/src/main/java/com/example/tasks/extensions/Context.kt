package com.example.tasks.extensions

import android.app.AlarmManager
import android.content.Context

fun Context.alarmManager() : AlarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager