package com.dleibovich.todokotlin.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import com.dleibovich.todokotlin.db.getToday
import java.util.*

const val ALARM_TIME = 8 // am

class AlarmController(val context: Context) {

    private val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun setupAlarm() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, ALARM_TIME)

        val intent = NotificationService.createIntent(context, getToday())
        val pending = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_NO_CREATE)
        alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.timeInMillis, 1000 * 60 * 20, pending)
    }
}

