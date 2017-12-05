package com.dleibovich.todokotlin.db

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class ItemsRepository(private val itemDao: TodoItemDao) {

    private val changeSubject = PublishSubject.create<List<TodoItem>>()

    private var items = listOf<TodoItem>()

    fun getItemChanges(): Observable<List<TodoItem>> {
        return changeSubject
    }

    fun getItems(): Observable<List<TodoItem>> {
        return itemDao
                .getAllItems()
                .doAfterSuccess({
                    this.items = items
                })
                .toObservable()
    }

    fun insertItem(item: TodoItem): Observable<Long> {
        return Observable
                .fromCallable { itemDao.insertItem(item) }
                .doOnNext {
                    reloadItemsAndNotify()
                }
    }

    private fun reloadItemsAndNotify() {
        itemDao.getAllItems()
                .doAfterSuccess { items ->
                    this.items = items
                    changeSubject.onNext(items)
                }.subscribe()
    }
}