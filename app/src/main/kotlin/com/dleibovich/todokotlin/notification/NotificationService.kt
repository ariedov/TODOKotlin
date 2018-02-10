package com.dleibovich.todokotlin.notification

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import com.dleibovich.todokotlin.R
import com.dleibovich.todokotlin.TodoApp
import com.dleibovich.todokotlin.db.TodoItem
import java.util.*
import javax.inject.Inject

const val channel = "default"
const val tag = "todoapp"

class NotificationService: Service(), Controller {

    @Inject
    lateinit var presenter: NotificationPresenter

    private lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()

        (application as TodoApp)
                .dataComponent()
                .inject(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            val date = intent.getSerializableExtra(KEY_DATE) as Date

            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

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
        items.forEach { item ->
            val notification = NotificationCompat.Builder(this, channel)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(item.title)
                    .setContentText(item.description)
                    .setOngoing(true)
                    .build()
            notificationManager.notify(item.id, notification)
        }
    }

    override fun showNoTasks() {
        val notification = NotificationCompat.Builder(this, channel)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(getString(R.string.no_plans_was_made))
                .build()
        notificationManager.notify(tag, 0, notification)
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