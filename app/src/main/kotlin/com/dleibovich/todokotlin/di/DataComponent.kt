package com.dleibovich.todokotlin.di

import com.dleibovich.todokotlin.add.ManageItemActivity
import com.dleibovich.todokotlin.calendar.CalendarActivity
import com.dleibovich.todokotlin.db.ItemsRepository
import com.dleibovich.todokotlin.calendar.CalendarPresenter
import com.dleibovich.todokotlin.manage.ManageItemPresenter
import com.dleibovich.todokotlin.notification.NotificationController
import com.dleibovich.todokotlin.notification.NotificationPresenter
import com.dleibovich.todokotlin.notification.NotificationService
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Subcomponent(modules = [(DataModule::class)])
interface DataComponent {

    fun inject(target: ManageItemActivity)

    fun inject(target: CalendarActivity)

    fun inject(target: NotificationService)
}

@Module
class DataModule {

    @Provides
    fun manageItemPresenter(itemsRepository: ItemsRepository,
                            notificationController: NotificationController): ManageItemPresenter {
        return ManageItemPresenter(itemsRepository, notificationController)
    }

    @Provides
    fun calendarPresenter(itemsRepository: ItemsRepository): CalendarPresenter {
        return CalendarPresenter(itemsRepository)
    }

    @Provides
    fun notificationPresenter(itemsRepository: ItemsRepository): NotificationPresenter {
        return NotificationPresenter(itemsRepository)
    }
}

