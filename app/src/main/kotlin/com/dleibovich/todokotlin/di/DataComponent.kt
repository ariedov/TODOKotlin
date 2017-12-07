package com.dleibovich.todokotlin.di

import com.dleibovich.todokotlin.add.AddItemActivity
import com.dleibovich.todokotlin.add.AddItemPresenter
import com.dleibovich.todokotlin.calendar.CalendarActivity
import com.dleibovich.todokotlin.db.ItemsRepository
import com.dleibovich.todokotlin.calendar.CalendarPresenter
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Subcomponent(modules = [(DataModule::class)])
interface DataComponent {

    fun inject(target: AddItemActivity)

    fun inject(target: CalendarActivity)
}

@Module
class DataModule {

    @Provides
    fun addItemPresenter(itemsRepository: ItemsRepository): AddItemPresenter {
        return AddItemPresenter(itemsRepository)
    }

    @Provides
    fun calendarPresenter(itemsRepository: ItemsRepository): CalendarPresenter {
        return CalendarPresenter(itemsRepository)
    }
}

