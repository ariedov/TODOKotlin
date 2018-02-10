package com.dleibovich.todokotlin.notification

import android.content.Context
import com.dleibovich.todokotlin.db.TodoItem
import com.dleibovich.todokotlin.db.getToday

class NotificationController(val context: Context) {

    fun updateNotificationWith(item: TodoItem) {
        if (item.date == getToday()) {
            context.startService(NotificationService.createIntent(context, getToday()))
        }
    }
}