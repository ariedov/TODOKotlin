package com.dleibovich.todokotlin.db

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.*

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

    fun getItemDates(): Observable<List<Date>> {
        return itemDao
                .getAllItems()
                .flatMapObservable {
                    val result = sortedSetOf<Date>()
                    it.forEach { result.add(it.date) }
                    Observable.just(result.toList())
                }
    }

    fun insertItem(item: TodoItem): Observable<Long> {
        return Observable
                .fromCallable { itemDao.insertItem(item) }
                .doOnNext { reloadItemsAndNotify() }
    }

    fun updateItem(item: TodoItem): Observable<Int> {
        return Observable
                .fromCallable { itemDao.updateItem(item) }
                .doOnNext { reloadItemsAndNotify() }
    }

    fun markAsDone(item: TodoItem): Observable<Int> {
        return Observable
                .fromCallable { itemDao.updateItem(item.copy(isDone = true)) }
                .doOnNext { reloadItemsAndNotify() }
    }

    fun delete(item: TodoItem): Observable<Int> {
        return Observable
                .fromCallable { itemDao.deleteItem(item) }
                .doOnNext { reloadItemsAndNotify() }
    }

    private fun reloadItemsAndNotify() {
        itemDao.getAllItems()
                .doAfterSuccess { items ->
                    changeSubject.onNext(items)
                }.subscribe()
    }
}