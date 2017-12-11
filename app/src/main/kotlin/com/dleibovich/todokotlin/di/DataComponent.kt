package com.dleibovich.todokotlin.di

import com.dleibovich.todokotlin.add.ManageItemActivity
import com.dleibovich.todokotlin.calendar.CalendarActivity
import com.dleibovich.todokotlin.db.ItemsRepository
import com.dleibovich.todokotlin.calendar.CalendarPresenter
import com.dleibovich.todokotlin.edit.ManageItemPresenter
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Subcomponent(modules = [(DataModule::class)])
interface DataComponent {

    fun inject(target: ManageItemActivity)

    fun inject(target: CalendarActivity)
}

@Module
class DataModule {

    @Provides
    fun manageItemPresenter(itemsRepository: ItemsRepository): ManageItemPresenter {
        return ManageItemPresenter(itemsRepository)
    }

    @Provides
    fun calendarPresenter(itemsRepository: ItemsRepository): CalendarPresenter {
        return CalendarPresenter(itemsRepository)
    }
}

