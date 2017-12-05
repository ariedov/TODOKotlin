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
                .toObservable()
                .doOnNext { items ->
                    this.items = items
                }
    }

    fun insertItem(item: TodoItem): Observable<Long> {
        return Observable
                .just(itemDao.insertItem(item))
                .doOnNext {
                    reloadItemsAndNotify()
                }
    }

    private fun reloadItemsAndNotify() {
        getItems()
                .doOnNext { items ->
                    changeSubject.onNext(items)
                }
    }
}