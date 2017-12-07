package com.dleibovich.todokotlin.di

import com.dleibovich.todokotlin.db.ItemsRepository
import com.dleibovich.todokotlin.list.ListFragment
import com.dleibovich.todokotlin.list.TodoListPresenter
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import java.util.*
import javax.inject.Scope

@ListScope @Subcomponent(modules = [ListModule::class])
interface ListComponent {

    fun inject(target: ListFragment)
}

@ListScope @Module
class ListModule(private val date: Date) {

    @ListScope @Provides
    fun todoListPresenter(itemsRepository: ItemsRepository): TodoListPresenter {
        return TodoListPresenter(itemsRepository, date)
    }
}

@Scope @Retention(AnnotationRetention.RUNTIME)
annotation class ListScope
