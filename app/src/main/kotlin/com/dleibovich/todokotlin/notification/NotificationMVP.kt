package com.dleibovich.todokotlin.notification

import com.dleibovich.todokotlin.db.ItemsRepository
import com.dleibovich.todokotlin.db.TodoItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*

interface Controller {

    fun showNotification(items: List<TodoItem>)

    fun showNoTasks()
}

class NotificationPresenter(private val repo: ItemsRepository) {

    private val updateDisposable: Disposable
    private lateinit var loadDisposable: Disposable
    private lateinit var controller: Controller

    init {
        updateDisposable = repo.getItemChanges()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it.isEmpty()) {
                        controller.showNoTasks()
                    } else {
                        controller.showNotification(it)
                    }
                }
    }

    fun updateList(date: Date) {
        loadDisposable = repo.getItems()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map { it.filter { it.date == date } }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it.isEmpty()) {
                        controller.showNoTasks()
                    } else {
                        controller.showNotification(it)
                    }
                }
    }

    fun stop() {
        loadDisposable.dispose()
        updateDisposable.dispose()
    }

    fun setController(controller: Controller) {
        this.controller = controller
    }
}