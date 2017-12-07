package com.dleibovich.todokotlin.di

import com.dleibovich.todokotlin.add.AddItemActivity
import com.dleibovich.todokotlin.add.AddItemPresenter
import com.dleibovich.todokotlin.db.ItemsRepository
import com.dleibovich.todokotlin.MainActivity
import com.dleibovich.todokotlin.list.ListFragment
import com.dleibovich.todokotlin.list.TodoListPresenter
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Subcomponent(modules = [(DataModule::class)])
interface DataComponent {

    fun inject(target: AddItemActivity)
}

@Module
class DataModule {

    @Provides
    fun addItemPresenter(itemsRepository: ItemsRepository): AddItemPresenter {
        return AddItemPresenter(itemsRepository)
    }
}

