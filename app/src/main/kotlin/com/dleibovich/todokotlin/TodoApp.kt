package com.dleibovich.todokotlin

import android.app.Application
import com.dleibovich.todokotlin.di.*

class TodoApp : Application() {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
    }

    fun dataComponent(): DataComponent {
        return appComponent.dataComponent()
    }
}