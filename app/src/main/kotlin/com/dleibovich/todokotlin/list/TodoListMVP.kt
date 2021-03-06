package com.dleibovich.todokotlin.list

import com.dleibovich.todokotlin.db.ItemsRepository
import com.dleibovich.todokotlin.db.TodoItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

interface TodoListView {

    fun setData(items: List<TodoItem>)

    fun setEmpty()

    fun setError()
}

class TodoListPresenter(private val repo: ItemsRepository, private val date: Date) {

    private val disposables = CompositeDisposable()
    private val stoppables = CompositeDisposable()
    private var view: TodoListView? = null

    private var items: List<TodoItem>? = null

    init {
        disposables.add(repo.getItemChanges()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.filter { it.date == date } }
                .map { it.sortedBy { it.isDone } }
                .subscribe { items ->
                    processItems(items)
                })
    }

    fun setView(view: TodoListView?) {
        this.view = view
    }

    fun requestItems() {
        if (items != null) {
            processItems(items!!)
            return
        }

        stoppables.add(repo.getItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it.filter { it.date == date } }
                .map { it.sortedBy { it.isDone } }
                .subscribe(
                        { items ->
                            processItems(items)
                        },
                        { _ ->
                            view?.setError()
                        }))
    }

    private fun processItems(items: List<TodoItem>) {
        this.items = items
        if (items.isNotEmpty()) {
            view?.setData(items)
        } else {
            view?.setEmpty()
        }
    }

    fun markAsDone(item: TodoItem) {
        disposables.add(repo.markAsDone(item)
                .subscribeOn(Schedulers.io())
                .subscribe())
    }

    fun delete(item: TodoItem) {
        disposables.add(repo.delete(item)
                .subscribeOn(Schedulers.io())
                .subscribe())
    }

    fun stop() {
        stoppables.dispose()
    }

    fun destroy() {
        disposables.dispose()
    }
}