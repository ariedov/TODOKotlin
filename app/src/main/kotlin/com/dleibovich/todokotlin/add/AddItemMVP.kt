package com.dleibovich.todokotlin.add

import com.dleibovich.todokotlin.db.ItemsRepository
import com.dleibovich.todokotlin.db.TodoItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

interface AddItemView {

    fun setSuccess()

    fun setError()
}

class AddItemPresenter(private val repo: ItemsRepository) {

    private val disposables = CompositeDisposable()
    private var view: AddItemView? = null

    fun setView(view: AddItemView?) {
        this.view = view
    }

    fun insertItem(item: TodoItem) {
        disposables.add(repo.insertItem(item)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { view?.setSuccess() },
                        { view?.setError() }))
    }

    fun destroy() {
        disposables.dispose()
    }
}