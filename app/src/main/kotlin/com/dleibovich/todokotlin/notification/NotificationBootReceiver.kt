package com.dleibovich.todokotlin.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dleibovich.todokotlin.db.getToday

class NotificationBootReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (Intent.ACTION_BOOT_COMPLETED == intent?.action && context != null) {
            context.startService(NotificationService.createIntent(context, getToday()))
        }
    }
}