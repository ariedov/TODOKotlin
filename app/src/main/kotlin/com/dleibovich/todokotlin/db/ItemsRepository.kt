package com.dleibovich.todokotlin.db

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class ItemsRepository(private val itemDao: TodoItemDao) {

    private val changeSubject = PublishSubject.create<List<TodoItem>>()

    fun getItemChanges(): Observable<List<TodoItem>> {
        return changeSubject
    }

    fun getItems(): Observable<List<TodoItem>> {
        return itemDao
                .getAllItems()
                .toObservable()
    }

    fun insertItem(item: TodoItem): Observable<Long> {
        return Observable
                .fromCallable { itemDao.insertItem(item) }
                .doOnNext {
                    reloadItemsAndNotify()
                }
    }

    fun markAsDone(item: TodoItem): Observable<Int> {
        return Observable
                .fromCallable { itemDao.updateItem(item.copy(isDone = true)) }
    }

    private fun reloadItemsAndNotify() {
        itemDao.getAllItems()
                .doAfterSuccess { items ->
                    changeSubject.onNext(items)
                }.subscribe()
    }
}