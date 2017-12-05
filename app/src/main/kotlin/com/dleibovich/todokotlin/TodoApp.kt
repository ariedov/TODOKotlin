package com.dleibovich.todokotlin

import android.app.Application
import com.dleibovich.todokotlin.di.AppComponent
import com.dleibovich.todokotlin.di.AppModule
import com.dleibovich.todokotlin.di.DaggerAppComponent
import com.dleibovich.todokotlin.di.ListComponent

class TodoApp : Application() {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
    }

    fun listComponent(): ListComponent {
        return appComponent.listComponent()
    }
}