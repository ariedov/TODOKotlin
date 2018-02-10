package com.dleibovich.todokotlin.di

import android.arch.persistence.room.Room
import android.content.Context
import com.dleibovich.todokotlin.db.AppDatabase
import com.dleibovich.todokotlin.db.ItemsRepository
import com.dleibovich.todokotlin.notification.NotificationController
import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton @Component(modules = [(AppModule::class)])
interface AppComponent {

    fun dataComponent(): DataComponent

    fun plus(listModule: ListModule): ListComponent
}

@Module
class AppModule(private val appContext: Context) {

    @Provides
    fun provideContext(): Context {
        return appContext
    }

    @Provides @Singleton
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room
                .databaseBuilder(context, AppDatabase::class.java, "todo-db")
                .build()
    }

    @Provides @Singleton
    fun provideItemsRepository(database: AppDatabase): ItemsRepository {
        return ItemsRepository(database.itemDao())
    }

    @Provides @Singleton
    fun provideNotificationController(context: Context): NotificationController {
        return NotificationController(context)
    }
}