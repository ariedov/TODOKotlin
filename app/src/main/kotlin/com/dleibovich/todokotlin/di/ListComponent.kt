package com.dleibovich.todokotlin.di

import com.dleibovich.todokotlin.db.ItemsRepository
import com.dleibovich.todokotlin.list.ListActivity
import com.dleibovich.todokotlin.list.TodoListPresenter
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Subcomponent(modules = [(ListModule::class)])
interface ListComponent {

    fun inject(target: ListActivity)
}

@Module
class ListModule {

    @Provides
    fun todoListPresenter(itemsRepository: ItemsRepository): TodoListPresenter {
        return TodoListPresenter(itemsRepository)
    }
}