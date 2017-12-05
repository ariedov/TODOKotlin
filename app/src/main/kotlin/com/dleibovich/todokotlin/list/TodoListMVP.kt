package com.dleibovich.todokotlin.list

import com.dleibovich.todokotlin.db.ItemsRepository
import com.dleibovich.todokotlin.db.TodoItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

interface TodoListView {

    fun setData(items: List<TodoItem>)

    fun setError()
}

class TodoListPresenter(private val repo: ItemsRepository) {

    private val disposables = CompositeDisposable()
    private val stoppables = CompositeDisposable()
    private var view: TodoListView? = null

    init {
        disposables.add(repo.getItemChanges()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { items ->
                    view?.setData(items)
                })
    }

    fun setView(view: TodoListView?) {
        this.view = view
    }

    fun requestItems() {
        stoppables.add(repo.getItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { items ->
                            view?.setData(items)
                        },
                        { _ ->
                            view?.setError()
                        }))
    }

    fun stop() {
        stoppables.dispose()
    }

    fun destroy() {
        disposables.dispose()
    }
}