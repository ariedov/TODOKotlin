package com.dleibovich.todokotlin

import android.app.Application
import com.dleibovich.todokotlin.db.getToday
import com.dleibovich.todokotlin.db.getTomorrow
import com.dleibovich.todokotlin.di.*
import com.dleibovich.todokotlin.notification.AlarmController
import com.dleibovich.todokotlin.notification.NotificationService
import java.util.*

class TodoApp : Application() {

    private lateinit var appComponent: AppComponent
    private var listComponents = mutableMapOf<Date, ListComponent>()

    private lateinit var alarmController: AlarmController

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()

        alarmController = AlarmController(this)
        alarmController.setupAlarm()

        startService(NotificationService.createIntent(this, getTomorrow()))
    }

    fun dataComponent(): DataComponent {
        return appComponent.dataComponent()
    }

    fun getListComponent(date: Date): ListComponent {
        var component = listComponents[date]
        if (component == null) {
            component = appComponent.plus(ListModule(date))
            listComponents[date] = component
        }
        return component
    }

    fun clearListComponent(date: Date) {
        listComponents.remove(date)
    }
}