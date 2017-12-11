package com.dleibovich.todokotlin.edit

import com.dleibovich.todokotlin.db.ItemsRepository
import com.dleibovich.todokotlin.db.TodoItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

interface EditItemView {

    fun setSuccess()

    fun setError()
}

class EditItemPresenter(private val repo: ItemsRepository) {

    private val disposables = CompositeDisposable()
    private var view: EditItemView? = null

    fun setView(view: EditItemView?) {
        this.view = view
    }

    fun updateItem(item: TodoItem) {
        disposables.add(repo.updateItem(item)
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