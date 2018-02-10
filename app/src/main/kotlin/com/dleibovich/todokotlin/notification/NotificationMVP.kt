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

    private var updateDisposable: Disposable? = null
    private var loadDisposable: Disposable? = null
    private lateinit var controller: Controller

    fun updateList(date: Date) {
        updateDisposable?.dispose()
        loadDisposable?.dispose()

        subscribeForUpdates(date)
        loadData(date)
    }

    private fun subscribeForUpdates(date: Date) {
        updateDisposable = repo.getItemChanges()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map { it.filter { it.date == date } }
                .map { it.filter { !it.isDone } }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it.isEmpty()) {
                        controller.showNoTasks()
                    } else {
                        controller.showNotification(it)
                    }
                }
    }

    private fun loadData(date: Date) {
        loadDisposable = repo.getItems()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map { it.filter { it.date == date } }
                .map { it.filter { !it.isDone } }
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
        loadDisposable?.dispose()
        updateDisposable?.dispose()
    }

    fun setController(controller: Controller) {
        this.controller = controller
    }
}