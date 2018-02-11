package com.dleibovich.todokotlin.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import com.dleibovich.todokotlin.R
import com.dleibovich.todokotlin.TodoApp
import com.dleibovich.todokotlin.db.TodoItem
import java.util.*
import javax.inject.Inject

const val channel = "default"
const val notificationId = 1

class NotificationService: Service(), Controller {

    @Inject
    lateinit var presenter: NotificationPresenter

    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationChannel: NotificationChannel

    override fun onCreate() {
        super.onCreate()

        (application as TodoApp)
                .dataComponent()
                .inject(this)

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = createNotificationChannel(notificationManager)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager): NotificationChannel {
        val notificationChannel = NotificationChannel(channel, getString(R.string.title_today),
                NotificationManager.IMPORTANCE_DEFAULT)
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        notificationManager.createNotificationChannel(notificationChannel)
        return notificationChannel
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            val date = intent.getSerializableExtra(KEY_DATE) as Date

            presenter.setController(this)
            presenter.updateList(date)
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter.stop()
    }

    override fun onBind(intent: Intent?): IBinder {
        return Binder()
    }

    override fun showNotification(items: List<TodoItem>) {
        notificationManager.cancelAll()

        val style = NotificationCompat.InboxStyle()
        items.forEach { item -> style.addLine(item.title) }

        val notification = NotificationCompat.Builder(this, channel)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(getString(R.string.title_today))
                .setStyle(style)
                .setOngoing(true)
                .build()
        notificationManager.notify(notificationId, notification)
    }

    override fun showNoTasks() {
        val notification = NotificationCompat.Builder(this, channel)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(getString(R.string.no_plans_was_made))
                .build()
        notificationManager.notify(notificationId, notification)
    }

    companion object {
        private const val KEY_DATE = "date"

        fun createIntent(context: Context, date: Date): Intent {
            val intent = Intent(context, NotificationService::class.java)
            intent.putExtra(KEY_DATE, date)
            return intent
        }
    }
}